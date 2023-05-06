package x590.jdecompiler.attribute.annotation;

import java.lang.constant.Constable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import x590.jdecompiler.Importable;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.ConstValueConstant;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.util.StringUtil;
import x590.jdecompiler.writable.StringifyWritable;
import x590.util.IntegerUtil;
import x590.util.annotation.Immutable;

public abstract class ElementValue implements StringifyWritable<ClassInfo>, Importable {
	
	public static class ConstElementValue extends ElementValue {
		
		private final Type type;
		private final ConstValueConstant value;
		
		private ConstElementValue(Type type, ExtendedDataInputStream in, ConstantPool pool) {
			this.type = type;
			this.value = pool.get(in.readUnsignedShort());
		}
		
		private ConstElementValue(Type type, ConstValueConstant value) {
			this.type = type;
			this.value = value;
		}
		
		private ConstElementValue(ConstantPool pool, byte value) {
			this(PrimitiveType.BYTE, pool.findOrCreateConstant(value));
		}
		
		private ConstElementValue(ConstantPool pool, short value) {
			this(PrimitiveType.SHORT, pool.findOrCreateConstant(value));
		}
		
		private ConstElementValue(ConstantPool pool, char value) {
			this(PrimitiveType.CHAR, pool.findOrCreateConstant(value));
		}
		
		private ConstElementValue(ConstantPool pool, int value) {
			this(PrimitiveType.INT, pool.findOrCreateConstant(value));
		}
		
		private ConstElementValue(ConstantPool pool, long value) {
			this(PrimitiveType.LONG, pool.findOrCreateConstant(value));
		}
		
		private ConstElementValue(ConstantPool pool, float value) {
			this(PrimitiveType.FLOAT, pool.findOrCreateConstant(value));
		}
		
		private ConstElementValue(ConstantPool pool, double value) {
			this(PrimitiveType.DOUBLE, pool.findOrCreateConstant(value));
		}
		
		private ConstElementValue(ConstantPool pool, boolean value) {
			this(PrimitiveType.BOOLEAN, pool.findOrCreateConstant(value));
		}
		
		public ConstValueConstant getConstant() {
			return value;
		}
		
		
		@Override
		public void addImports(ClassInfo classinfo) {
			value.addImports(classinfo);
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			value.writeTo(out, classinfo, type);
		}
		
		
		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof ConstElementValue elementValue && this.equals(elementValue);
		}
		
		public boolean equals(ConstElementValue other) {
			return this == other || type.equals(other.type) && value.equals(other.value);
		}
	}
	
	
	public static class StringElementValue extends ElementValue {
		
		private final String value;
		
		private StringElementValue(ExtendedDataInputStream in, ConstantPool pool) {
			this.value = pool.getUtf8String(in.readUnsignedShort());
		}
		
		private StringElementValue(String value) {
			this.value = value;
		}
		
		public String getString() {
			return value;
		}
		
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.write(StringUtil.toLiteral(value));
		}
		
		
		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof StringElementValue elementValue && this.equals(elementValue);
		}
		
		public boolean equals(StringElementValue other) {
			return this == other || value.equals(other.value);
		}
	}
	
	
	public static class EnumElementValue extends ElementValue {
		
		private final ClassType type;
		private final String constantName;
		
		private EnumElementValue(ExtendedDataInputStream in, ConstantPool pool) {
			this.type = ClassType.fromTypeDescriptor(pool.getUtf8String(in.readUnsignedShort()));
			this.constantName = pool.getUtf8String(in.readUnsignedShort());
		}
		
		private EnumElementValue(ClassType type, String constantName) {
			this.type = type;
			this.constantName = constantName;
		}
		
		public ClassType getType() {
			return type;
		}
		
		public String getConstantName() {
			return constantName;
		}
		
		
		@Override
		public void addImports(ClassInfo classinfo) {
			classinfo.addImport(type);
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.print(type, classinfo).print('.').print(constantName);
		}
		
		
		
		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof EnumElementValue elementValue && this.equals(elementValue);
		}
		
		public boolean equals(EnumElementValue other) {
			return this == other || this.type.equals(other.type) && this.constantName.equals(other.constantName);
		}
	}
	
	
	public static class ClassElementValue extends ElementValue {
		
		private final ClassType classType;
		
		private ClassElementValue(ExtendedDataInputStream in, ConstantPool pool) {
			this.classType = ClassType.fromTypeDescriptor(pool.getUtf8String(in.readUnsignedShort()));
		}
		
		private ClassElementValue(ClassType classType) {
			this.classType = classType;
		}
		
		public ClassType getClassType() {
			return classType;
		}
		
		
		@Override
		public void addImports(ClassInfo classinfo) {
			classinfo.addImport(classType);
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.print(classType, classinfo).print(".class");
		}
		
		
		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof ClassElementValue elementValue && this.equals(elementValue);
		}
		
		public boolean equals(ClassElementValue other) {
			return this == other || classType.equals(other.classType);
		}
	}
	
	
	public static class AnnotationElementValue extends ElementValue {
		
		private final Annotation annotation;
		
		private AnnotationElementValue(ExtendedDataInputStream in, ConstantPool pool) {
			this.annotation = Annotation.read(in, pool);
		}
		
		private AnnotationElementValue(Annotation annotation) {
			this.annotation = annotation;
		}
		
		public Annotation getAnnotation() {
			return annotation;
		}

		
		@Override
		public void addImports(ClassInfo classinfo) {
			annotation.addImports(classinfo);
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.print(annotation, classinfo);
		}
		
		
		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof AnnotationElementValue elementValue && this.equals(elementValue);
		}
		
		public boolean equals(AnnotationElementValue other) {
			return this == other || annotation.equals(other.annotation);
		}
	}
	
	
	public static class ArrayElementValue extends ElementValue {
		
		private final @Immutable List<? extends ElementValue> values;
		
		private ArrayElementValue(ExtendedDataInputStream in, ConstantPool pool) {
			this.values = in.readImmutableList(() -> ElementValue.read(in, pool));
		}
		
		private ArrayElementValue(@Immutable List<? extends ElementValue> values) {
			this.values = values;
		}
		
		public @Immutable List<? extends ElementValue> getValues() {
			return values;
		}
		
		
		@Override
		public void addImports(ClassInfo classinfo) {
			classinfo.addImportsFor(values);
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			
			if(values.isEmpty()) {
				out.write("{}");
				
			} else if(values.size() == 1) {
				out.print(values.get(0), classinfo);
				
			} else {
				out.print("{ ").printAll(values, classinfo, ", ").print(" }");
			}
		}
		
		
		
		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof ArrayElementValue elementValue && this.equals(elementValue);
		}
		
		public boolean equals(ArrayElementValue other) {
			return this == other || values.equals(other.values);
		}
	}
	
	
	@Override
	public abstract boolean equals(Object other);
	
	
	protected static ElementValue read(ExtendedDataInputStream in, ConstantPool pool) {
		
		char tag = (char)in.readUnsignedByte();
		
		switch(tag) {
			case 'B': return new ConstElementValue(PrimitiveType.BYTE, in, pool);
			case 'S': return new ConstElementValue(PrimitiveType.SHORT, in, pool);
			case 'C': return new ConstElementValue(PrimitiveType.CHAR, in, pool);
			case 'I': return new ConstElementValue(PrimitiveType.INT, in, pool);
			case 'J': return new ConstElementValue(PrimitiveType.LONG, in, pool);
			case 'F': return new ConstElementValue(PrimitiveType.FLOAT, in, pool);
			case 'D': return new ConstElementValue(PrimitiveType.DOUBLE, in, pool);
			case 'Z': return new ConstElementValue(PrimitiveType.BOOLEAN, in, pool);
			case 's': return new StringElementValue(in, pool);
			case 'e': return new EnumElementValue(in, pool);
			case 'c': return new ClassElementValue(in, pool);
			case '@': return new AnnotationElementValue(in, pool);
			case '[': return new ArrayElementValue(in, pool);
			default:
				throw new DisassemblingException("Illegal anntotation element value tag: " +
						"'" + tag + "' (" + IntegerUtil.hex1WithPrefix(tag) + ")");
		}
	}
	
	protected static ElementValue fromUnknownValue(ConstantPool pool, Object value) {
		
		if(value instanceof Constable) {
			
			if(value instanceof Byte num) return new ConstElementValue(pool, num);
			if(value instanceof Short num) return new ConstElementValue(pool, num);
			if(value instanceof Integer num) return new ConstElementValue(pool, num);
			if(value instanceof Character chr) return new ConstElementValue(pool, chr);
			if(value instanceof Long num) return new ConstElementValue(pool, num);
			if(value instanceof Float num) return new ConstElementValue(pool, num);
			if(value instanceof Double num) return new ConstElementValue(pool, num);
			if(value instanceof Boolean bool) return new ConstElementValue(pool, bool);
			
			if(value instanceof String str)
				return new StringElementValue(str);
			
			if(value instanceof Enum<?> en)
				return new EnumElementValue(ClassType.fromClass(en.getDeclaringClass()), en.name());
			
			if(value instanceof Class<?> clazz)
				return new ClassElementValue(ClassType.fromClass(clazz));
			
		} else {
			
			if(value instanceof java.lang.annotation.Annotation annotation)
				return new AnnotationElementValue(Annotation.fromReflectAnnotation(pool, annotation));
			
			if(value.getClass().isArray()) {
				
				if(value instanceof Object[] array)
					return new ArrayElementValue(arrayToElementValues(array.length, i -> fromUnknownValue(pool, array[i])));
				
				if(value instanceof byte[] array)
					return new ArrayElementValue(arrayToElementValues(array.length, i -> new ConstElementValue(pool, array[i])));
				
				if(value instanceof short[] array)
					return new ArrayElementValue(arrayToElementValues(array.length, i -> new ConstElementValue(pool, array[i])));
				
				if(value instanceof char[] array)
					return new ArrayElementValue(arrayToElementValues(array.length, i -> new ConstElementValue(pool, array[i])));
				
				if(value instanceof int[] array)
					return new ArrayElementValue(arrayToElementValues(array.length, i -> new ConstElementValue(pool, array[i])));
				
				if(value instanceof long[] array)
					return new ArrayElementValue(arrayToElementValues(array.length, i -> new ConstElementValue(pool, array[i])));
				
				if(value instanceof float[] array)
					return new ArrayElementValue(arrayToElementValues(array.length, i -> new ConstElementValue(pool, array[i])));
				
				if(value instanceof double[] array)
					return new ArrayElementValue(arrayToElementValues(array.length, i -> new ConstElementValue(pool, array[i])));
				
				if(value instanceof boolean[] array)
					return new ArrayElementValue(arrayToElementValues(array.length, i -> new ConstElementValue(pool, array[i])));
			}
		}
		
		throw new IllegalArgumentException("Object " + value + " is not an annotation field");
	}
	
	
	private static List<ElementValue> arrayToElementValues(int size, IntFunction<? extends ElementValue> elementSuppiler) {
		List<ElementValue> list = new ArrayList<>(size);
		
		for(int i = 0; i < size; i++) {
			list.add(elementSuppiler.apply(i));
		}
		
		return list;
	}
}
