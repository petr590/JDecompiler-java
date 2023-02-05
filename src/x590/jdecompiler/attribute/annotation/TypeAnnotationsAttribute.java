package x590.jdecompiler.attribute.annotation;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.Importable;
import x590.jdecompiler.StringWritable;
import x590.jdecompiler.attribute.Attribute;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.util.ArrayUtil;

// Слишком сложно
public class TypeAnnotationsAttribute extends Attribute {
	
	private final TypeAnnotation[] parametersAnnotations;
	
	public TypeAnnotationsAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		this(nameIndex, name, length, in.readArray(TypeAnnotation[]::new, () -> new TypeAnnotation(in, pool)));
	}
	
	public TypeAnnotationsAttribute(int nameIndex, String name, int length, TypeAnnotation[] parametersAnnotations) {
		super(nameIndex, name, length);
		this.parametersAnnotations = parametersAnnotations;
	}
	
	
	public static TypeAnnotationsAttribute emptyVisible() {
		return EmptyParameterAnnotationsAttribute.VISIBLE;
	}
	
	public static TypeAnnotationsAttribute emptyInvisible() {
		return EmptyParameterAnnotationsAttribute.INVISIBLE;
	}
	
	
	public boolean isEmpty() {
		return false;
	}
	
	
	public static final class EmptyParameterAnnotationsAttribute extends TypeAnnotationsAttribute {
		
		private static final TypeAnnotation[] EMPTY_PARAMETERS = {};
		
		public static final TypeAnnotationsAttribute
				VISIBLE = new EmptyParameterAnnotationsAttribute("RuntimeVisibleParameterAnnotations"),
				INVISIBLE = new EmptyParameterAnnotationsAttribute("RuntimeInvisibleParameterAnnotations");
		
		public EmptyParameterAnnotationsAttribute(String name) {
			super(0, name, 0, EMPTY_PARAMETERS);
		}
		
		@Override
		public boolean isEmpty() {
			return true;
		}
		
		@Override
		public void write(StringifyOutputStream out, ClassInfo classinfo, int index) {}
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		ArrayUtil.forEach(parametersAnnotations, parameterAnnotations -> parameterAnnotations.addImports(classinfo));
	}
	
	public void write(StringifyOutputStream out, ClassInfo classinfo, int index) {
		if(index < parametersAnnotations.length && parametersAnnotations[index] != null)
			parametersAnnotations[index].writeTo(out, classinfo);
	}
	
	
	public static class TypeAnnotation implements StringWritable<ClassInfo>, Importable {
		
		public TypeAnnotation(ExtendedDataInputStream in, ConstantPool pool) {
			switch(in.readUnsignedByte()) {
				// ???
			}
			
			
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			
		}
	}
}
