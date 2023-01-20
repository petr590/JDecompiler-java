package x590.jdecompiler.attribute;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import x590.jdecompiler.attribute.Attributes.Location;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.type.ClassType;
import x590.util.annotation.Immutable;

public class CodeAttribute extends Attribute {
	
	public final int maxStack, maxLocals;
	public final byte[] code;
	public final ExceptionTable exceptionTable;
	public final Attributes attributes;
	
	CodeAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		
		this.maxStack = in.readUnsignedShort();
		this.maxLocals = in.readUnsignedShort();
		
		this.code = new byte[in.readInt()];
		in.readFully(code);
		
		this.exceptionTable = new ExceptionTable(in, pool);
		this.attributes = Attributes.read(in, pool, Location.CODE_ATTRIBUTE);
	}
	
	CodeAttribute(int nameIndex, String name, int length, int maxStack, int maxLocals, byte[] code, ExceptionTable exceptionTable, Attributes attributes) {
		super(nameIndex, name, length);
		
		this.maxStack = maxStack;
		this.maxLocals = maxLocals;
		this.code = code;
		this.exceptionTable = exceptionTable;
		this.attributes = attributes;
	}
	
	public boolean isEmpty() {
		return false;
	}
	
	
	public static class ExceptionTable {
		
		private static final ExceptionTable EMPTY_TABLE = new ExceptionTable();
		
		private final @Immutable List<CatchEntry> entries;
		
		private ExceptionTable() {
			this.entries = Collections.emptyList();
		}
		
		public ExceptionTable(ExtendedDataInputStream in, ConstantPool pool) {
			this.entries = Collections.unmodifiableList(in.readArrayList(() -> new CatchEntry(in, pool)));
		}
		
		
		public static ExceptionTable empty() {
			return EMPTY_TABLE;
		}
		
		
		public static class CatchEntry {
			
			public final int tryStartPos, tryEndPos, catchStartPos;
			public final ClassType type;
	        
			private CatchEntry(ExtendedDataInputStream in, ConstantPool pool) {
				this.tryStartPos = in.readUnsignedShort();
				this.tryEndPos = in.readUnsignedShort();
				this.catchStartPos = in.readUnsignedShort();
				this.type = pool.getClassConstant(in.readUnsignedShort()).toClassType();
			}
		}
	}
	
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		
		out.writeShort(maxStack);
		out.writeShort(maxLocals);
		
		out.writeInt(code.length);
		out.write(code);
	}
}
