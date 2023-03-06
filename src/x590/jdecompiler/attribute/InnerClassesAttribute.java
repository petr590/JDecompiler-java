package x590.jdecompiler.attribute;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.modifiers.ClassModifiers;
import x590.jdecompiler.type.ClassType;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public class InnerClassesAttribute extends Attribute {
	
	private static final InnerClassesAttribute EMPTY = new InnerClassesAttribute();
	
	private final @Immutable Map<ClassType, InnerClassEntry> entries;
	
	private InnerClassesAttribute() {
		super(0, AttributeNames.INNER_CLASSES, 0);
		this.entries = Collections.emptyMap();
	}
	
	protected InnerClassesAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		
		int size = in.readUnsignedShort();
		Map<ClassType, InnerClassEntry> entries = new HashMap<>(size);
		
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
	
	public @Nullable InnerClassEntry find(ClassType type) {
		return entries.get(type);
	}
	
	
	public static class InnerClassEntry {
		
		private final ClassType innerType, outerType;
		private final String name;
		private final ClassModifiers modifiers;
		
		private InnerClassEntry(ExtendedDataInputStream in, ConstantPool pool) {
			this.innerType = pool.getClassConstant(in.readUnsignedShort()).toClassType();
			this.outerType = pool.getClassConstant(in.readUnsignedShort()).toClassType();
			this.name = pool.getUtf8String(in.readUnsignedShort());
			this.modifiers = ClassModifiers.read(in);
		}
		
		public ClassType getInnerType() {
			return innerType;
		}
		
		public ClassType getOuterType() {
			return outerType;
		}
		
		public String getName() {
			return name;
		}
		
		public ClassModifiers getModifiers() {
			return modifiers;
		}
	}
}
