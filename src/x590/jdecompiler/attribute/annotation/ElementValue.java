package x590.jdecompiler.attribute.annotation;

import java.util.List;

import x590.jdecompiler.Importable;
import x590.jdecompiler.StringifyWritable;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.ConstValueConstant;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.util.StringUtil;
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
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			value.writeTo(out, classinfo, type);
		}

		
		@Override
		public void addImports(ClassInfo classinfo) {
			value.addImports(classinfo);
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
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.print(type, classinfo).print('.').print(constantName);
		}
		
		
		@Override
		public void addImports(ClassInfo classinfo) {
			classinfo.addImport(type);
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
		
		private final ClassType clazz;
		
		private ClassElementValue(ExtendedDataInputStream in, ConstantPool pool) {
			this.clazz = ClassType.fromTypeDescriptor(pool.getUtf8String(in.readUnsignedShort()));
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.print(clazz, classinfo).print(".class");
		}
		
		
		@Override
		public void addImports(ClassInfo classinfo) {
			classinfo.addImport(clazz);
		}
		
		
		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof ClassElementValue elementValue && this.equals(elementValue);
		}
		
		public boolean equals(ClassElementValue other) {
			return this == other || clazz.equals(other.clazz);
		}
	}
	
	
	public static class AnnotationElementValue extends ElementValue {
		
		private final Annotation annotation;
		
		private AnnotationElementValue(ExtendedDataInputStream in, ConstantPool pool) {
			this.annotation = new Annotation(in, pool);
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.print(annotation, classinfo);
		}
		
		
		@Override
		public void addImports(ClassInfo classinfo) {
			annotation.addImports(classinfo);
		}
		
		
		@Override
		public boolean equals(Object other) {
			return this == other || other instanceof AnnotationElementValue elementValue && this.equals(elementValue);
		}
		
		public boolean equals(AnnotationElementValue other) {
			return this == other || this.annotation.equals(other.annotation);
		}
	}
	
	
	public static class ArrayElementValue extends ElementValue {
		
		private final @Immutable List<ElementValue> values;
		
		private ArrayElementValue(ExtendedDataInputStream in, ConstantPool pool) {
			this.values = in.readImmutableList(() -> ElementValue.read(in, pool));
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
		public void addImports(ClassInfo classinfo) {
			classinfo.addImportsFor(values);
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
			case 'C': return new ConstElementValue(PrimitiveType.CHAR, in, pool);
			case 'S': return new ConstElementValue(PrimitiveType.SHORT, in, pool);
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
				throw new DisassemblingException("Illegal anntotation element value tag: '" + tag + "' (" + IntegerUtil.hex1WithPrefix(tag) + ")");
		}
	}
}
