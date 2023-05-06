package x590.jdecompiler.attribute;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import x590.jdecompiler.constpool.ClassConstant;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.modifiers.ClassModifiers;
import x590.jdecompiler.type.reference.ClassType;
import x590.util.Util;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public class InnerClassesAttribute extends Attribute {
	
	private static final InnerClassesAttribute EMPTY = new InnerClassesAttribute();
	
	private final @Immutable Map<ClassType, InnerClassEntry> entries;
	
	private InnerClassesAttribute() {
		super(AttributeNames.INNER_CLASSES, 0);
		this.entries = Collections.emptyMap();
	}
	
	protected InnerClassesAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(name, length);
		
		int size = in.readUnsignedShort();
		Map<ClassType, InnerClassEntry> entries = new LinkedHashMap<>(size);
		
		for(int i = 0; i < size; i++) {
			InnerClassEntry entry = new InnerClassEntry(in, pool);
			
			if(entries.put(entry.innerType, entry) != null) {
				throw new DisassemblingException("Duplicate record in the \"" + AttributeNames.INNER_CLASSES + "\" attribute for class \"" + entry.innerType + "\"");
			}
		}
		
		this.entries = Collections.unmodifiableMap(entries);
	}
	
	
	public static InnerClassesAttribute empty() {
		return EMPTY;
	}
	
	
	public @Immutable Map<ClassType, InnerClassEntry> getEntries() {
		return entries;
	}
	
	public @Nullable InnerClassEntry find(ClassType innerType) {
		return entries.get(innerType);
	}
	
	
	public Stream<InnerClassEntry> getEntryStreamWithOuterType(ClassType outerType) {
		return entries.values().stream()
				.filter(entry -> entry.hasOuterType() && entry.getOuterType().equals(outerType));
	}
	
	
	public static class InnerClassEntry {
		
		private final ClassType innerType;
		private final @Nullable ClassType outerType;
		private final @Nullable String name;
		private final ClassModifiers modifiers;
		
		private InnerClassEntry(ExtendedDataInputStream in, ConstantPool pool) {
			this.innerType = pool.getClassConstant(in.readUnsignedShort()).toClassType();
			this.outerType = Util.getIfNonNull(pool.getNullable(in.readUnsignedShort()), ClassConstant::toClassType);
			this.name = pool.getNullableUtf8String(in.readUnsignedShort());
			this.modifiers = ClassModifiers.read(in);
		}
		
		public ClassType getInnerType() {
			return innerType;
		}
		
		public boolean hasOuterType() {
			return outerType != null;
		}
		
		public @Nullable ClassType getOuterType() {
			return outerType;
		}
		
		public @Nullable String getName() {
			return name;
		}
		
		public ClassModifiers getModifiers() {
			return modifiers;
		}
	}
}
