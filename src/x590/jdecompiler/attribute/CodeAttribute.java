package x590.jdecompiler.attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import x590.jdecompiler.JavaSerializable;
import x590.jdecompiler.attribute.Attributes.Location;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.PreDecompilationContext;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedDataOutputStream;
import x590.jdecompiler.scope.CatchScope;
import x590.jdecompiler.scope.FinallyScope;
import x590.jdecompiler.scope.Scope;
import x590.jdecompiler.scope.TryScope;
import x590.jdecompiler.type.ClassType;
import x590.util.LoopUtil;
import x590.util.Util;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public class CodeAttribute extends Attribute {
	
	private final int maxStackSize, maxLocalsCount;
	private final byte[] code;
	private final ExceptionTable exceptionTable;
	private final Attributes attributes;
	
	CodeAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(name, length);
		
		this.maxStackSize = in.readUnsignedShort();
		this.maxLocalsCount = in.readUnsignedShort();
		
		this.code = new byte[in.readInt()];
		in.readFully(code);
		
		this.exceptionTable = new ExceptionTable(in, pool);
		this.attributes = Attributes.read(in, pool, Location.CODE_ATTRIBUTE);
	}
	
	protected CodeAttribute(String name, int length, int maxStack, int maxLocals, byte[] code, ExceptionTable exceptionTable, Attributes attributes) {
		super(name, length);
		
		this.maxStackSize = maxStack;
		this.maxLocalsCount = maxLocals;
		this.code = code;
		this.exceptionTable = exceptionTable;
		this.attributes = attributes;
	}
	
	public int getMaxStackSize() {
		return maxStackSize;
	}
	
	public int getMaxLocalsCount() {
		return maxLocalsCount;
	}
	
	public byte[] getCode() {
		return code;
	}
	
	public ExceptionTable getExceptionTable() {
		return exceptionTable;
	}
	
	public Attributes getAttributes() {
		return attributes;
	}
	
	public static EmptyCodeAttribute empty() {
		return EmptyCodeAttribute.INSTANCE;
	}
	
	public boolean isEmpty() {
		return false;
	}
	
	
	public static class ExceptionTable implements JavaSerializable {
		
		private static final ExceptionTable EMPTY_TABLE = new ExceptionTable();
		
		private final @Immutable List<TryEntry> entries;
		
		private ExceptionTable() {
			this.entries = Collections.emptyList();
		}
		
		public ExceptionTable(ExtendedDataInputStream in, ConstantPool pool) {
			int size = in.readUnsignedShort();
			
			List<TryEntry> entries = new ArrayList<>(size);
			
			for(int i = 0; i < size; i++) {
				TryEntry.readTo(in, pool, entries);
			}
			
			Collections.sort(entries);
			entries.forEach(TryEntry::freeze);
			
			this.entries = Collections.unmodifiableList(entries);
		}
		
		public static ExceptionTable empty() {
			return EMPTY_TABLE;
		}
		
		public @Immutable List<TryEntry> getEntries() {
			return entries;
		}
		
		@Override
		public void serialize(ExtendedDataOutputStream out) {
			out.writeAll(entries);
		}
		
		
		public static class TryEntry implements Comparable<TryEntry>, JavaSerializable {
			private final int startPos, endPos;
			private @Immutable List<CatchEntry> catchEntries = new ArrayList<>();
			
			private TryEntry(int startPos, int endPos) {
				this.startPos = startPos;
				this.endPos = endPos;
			}
			
			public void addCatchEntry(@Nullable CatchEntry catchEntry) {
				if(catchEntry!= null)
					catchEntries.add(catchEntry);
			}
			
			public int getStartPos() {
				return startPos;
			}
			
			public int getEndPos() {
				return endPos;
			}
			
			public int getStartIndex(PreDecompilationContext context) {
				return context.posToIndex(startPos);
			}
			
			public int getEndIndex(PreDecompilationContext context) {
				return context.posToIndex(endPos);
			}
			
			public int getFactualEndIndex(PreDecompilationContext context) {
				return getEndIndex(context) - (isFinally() ? 1 : 0);
			}
			
			public @Immutable List<CatchEntry> getCatchEntries() {
				return catchEntries;
			}
			
			public boolean isFinally() {
				return catchEntries.stream().allMatch(CatchEntry::isFinally);
			}
			
			public void setLastPos(int lastCatchEntryEndPos) {
				catchEntries.get(catchEntries.size() - 1).setEndPos(lastCatchEntryEndPos);
			}
			
			private void freeze() {
				Collections.sort(catchEntries);
				
				LoopUtil.forEachPair(catchEntries, (entry1, entry2) -> {
					entry1.setEndPos(entry2.getStartPos());
					entry1.setHasNext();
				});
				
				catchEntries.forEach(CatchEntry::freeze);
				this.catchEntries = Collections.unmodifiableList(catchEntries);
			}
			
			private static void readTo(ExtendedDataInputStream in, ConstantPool pool, List<TryEntry> entries) {
				int startPos = in.readUnsignedShort(),
					endPos = in.readUnsignedShort();
				
				TryEntry tryEntry = entries.stream()
						.filter(entry -> entry.startPos == startPos && entry.endPos == endPos).findAny()
						.orElseGet(() -> Util.addAndGetBack(entries, new TryEntry(startPos, endPos)));
				
				CatchEntry.readTo(in, pool, tryEntry.catchEntries, entries);
			}
			
			public Scope createScope(DecompilationContext context) {
				return new TryScope(context, context.posToIndex(getEndPos()));
			}
			
			@Override
			public int compareTo(TryEntry other) {
				int diff = other.startPos - startPos;
				if(diff != 0)
					return diff;
				
				return other.endPos - endPos;
			}
			
			@Override
			public void serialize(ExtendedDataOutputStream out) {
				catchEntries.forEach(entry -> {
					out.writeShort(startPos);
					out.writeShort(endPos);
					out.writeShort(entry.startPos);
					out.writeShort(entry.exceptionTypeIndex);
				});
			}
		}
		
		
		public static class CatchEntry implements Comparable<CatchEntry> {
			
			private static final int NPOS = -1;
			
			private final int startPos, exceptionTypeIndex;
			private @Immutable List<ClassType> exceptionTypes = new ArrayList<>();
			private int endPos = NPOS;
			private boolean hasNext;
			
			private void addExceptionType(ConstantPool pool, int exceptionTypeIndex) {
				if(exceptionTypeIndex != 0) {
					exceptionTypes.add(pool.getClassConstant(exceptionTypeIndex).toClassType());
				}
			}
	        
			private CatchEntry(int startPos, int exceptionTypeIndex) {
				this.startPos = startPos;
				this.exceptionTypeIndex = exceptionTypeIndex;
			}
			
			public int getStartPos() {
				return startPos;
			}
			
			public int getEndPos() {
				return endPos;
			}
			
			public int getEndIndex(DecompilationContext context) {
				var endPos = this.endPos;
				return endPos != NPOS ? context.posToIndex(endPos) : context.currentScope().endIndex();
			}
			
			public @Immutable List<ClassType> getExceptionTypes() {
				return exceptionTypes;
			}
			
			public boolean isFinally() {
				return exceptionTypes.isEmpty();
			}
			
			public boolean hasNext() {
				return hasNext;
			}
			
			public void setHasNext() {
				hasNext = true;
			}
			
			private void setEndPos(int endPos) {
				this.endPos = endPos;
			}
			
			private void freeze() {
				this.exceptionTypes = Collections.unmodifiableList(exceptionTypes);
			}
			
			private static void readTo(ExtendedDataInputStream in, ConstantPool pool, List<CatchEntry> entries, List<TryEntry> tryEntries) {
				
				int startPos = in.readUnsignedShort(),
					exceptionTypeIndex = in.readUnsignedShort();
				
				CatchEntry catchEntry = tryEntries.stream()
						.flatMap(tryEntry -> tryEntry.catchEntries.stream())
						.filter(entry -> entry.startPos == startPos).findAny()
						.orElseGet(() -> Util.addAndGetBack(entries, new CatchEntry(startPos, exceptionTypeIndex)));
				
				catchEntry.addExceptionType(pool, exceptionTypeIndex);
			}
			
			public Scope createScope(DecompilationContext context) {
				return isFinally() ?
						new FinallyScope(context, getEndIndex(context), hasNext) :
						new CatchScope(context, getEndIndex(context), exceptionTypes, hasNext);
			}
			
			@Override
			public int compareTo(CatchEntry other) {
				return startPos - other.startPos;
			}
		}
	}
	
	
	@Override
	public void serialize(ExtendedDataOutputStream out) {
		serializeHeader(out);
		out.writeShort(maxStackSize);
		out.writeShort(maxLocalsCount);
		out.writeByteArrayIntSized(code);
		out.write(exceptionTable);
	}
}
