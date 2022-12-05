package x590.javaclass.attribute.annotation;

import x590.javaclass.ClassInfo;
import x590.javaclass.StringWritableAndImportable;
import x590.javaclass.constpool.ConstValueConstant;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.exception.DisassemblingException;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;
import x590.javaclass.util.Util;

public abstract class ElementValue implements StringWritableAndImportable {
	
	public static class ConstElementValue extends ElementValue {
		
		private final Type type;
		private final ConstValueConstant value;
		
		private ConstElementValue(Type type, ExtendedDataInputStream in, ConstantPool pool) {
			this.type = type;
			this.value = pool.get(in.readUnsignedShort());
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.write(value.toStringAs(type, classinfo));
		}
	}
	
	
	public static class EnumElementValue extends ElementValue {
		
		private final ClassType type;
		private final String constantName;
		
		private EnumElementValue(ExtendedDataInputStream in, ConstantPool pool) {
			this.type = ClassType.valueOfEncoded(pool.getUtf8String(in.readUnsignedShort()));
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
	}
	
	
	public static class ClassElementValue extends ElementValue {
		
		private final ClassType clazz;
		
		private ClassElementValue(ExtendedDataInputStream in, ConstantPool pool) {
			this.clazz = ClassType.valueOf(pool.getUtf8String(in.readUnsignedShort()));
		}

		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.print(clazz, classinfo).print(".class");
		}
		
		
		@Override
		public void addImports(ClassInfo classinfo) {
			classinfo.addImport(clazz);
		}
	}
	
	
	public static class AnnotationElementValue extends ElementValue {
		
		private final Annotation annotation;
		
		private AnnotationElementValue(ExtendedDataInputStream in, ConstantPool pool) {
			this.annotation = new Annotation(in, pool);
		}

		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.write(annotation, classinfo);
		}
		
		
		@Override
		public void addImports(ClassInfo classinfo) {
			annotation.addImports(classinfo);
		}
	}
	
	
	public static class ArrayElementValue extends ElementValue {
		
		private final ElementValue[] values;
		
		private ArrayElementValue(ExtendedDataInputStream in, ConstantPool pool) {
			this.values = in.readArray(ElementValue[]::new, () -> ElementValue.read(in, pool));
		}

		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			out.write('{');
			
			Util.forEachExcludingLast(values,
					element -> out.write(element, classinfo),
					() -> out.write(", "));
			
			out.write('}');
		}
		
		
		@Override
		public void addImports(ClassInfo classinfo) {
			Util.forEach(values, value -> value.addImports(classinfo));
		}
	}
	
	
	protected static ElementValue read(ExtendedDataInputStream in, ConstantPool pool) {
		
		char tag = (char)in.readUnsignedByte();
		
		switch(tag) {
			case 'B': return new ConstElementValue(PrimitiveType.BYTE, in, pool);
			case 'C': return new ConstElementValue(PrimitiveType.CHAR, in, pool);
			case 'S': return new ConstElementValue(PrimitiveType.SHORT, in, pool);
			case 'I': return new ConstElementValue(PrimitiveType.INT, in, pool);
			case 'L': return new ConstElementValue(PrimitiveType.LONG, in, pool);
			case 'F': return new ConstElementValue(PrimitiveType.FLOAT, in, pool);
			case 'D': return new ConstElementValue(PrimitiveType.DOUBLE, in, pool);
			case 's': return new ConstElementValue(ClassType.STRING, in, pool);
			case 'e': return new EnumElementValue(in, pool);
			case 'c': return new ClassElementValue(in, pool);
			case '@': return new AnnotationElementValue(in, pool);
			case '[': return new ArrayElementValue(in, pool);
			default:
				throw new DisassemblingException("Illegal anntotation element value tag: '" + tag + "' (" + Util.hex1WithPrefix(tag) + ")");
		}
	}
}