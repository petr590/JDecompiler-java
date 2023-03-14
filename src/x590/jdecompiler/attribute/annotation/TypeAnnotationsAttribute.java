package x590.jdecompiler.attribute.annotation;

import java.util.Collections;
import java.util.List;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.Importable;
import x590.jdecompiler.StringifyWritable;
import x590.jdecompiler.attribute.Attribute;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.util.annotation.Immutable;

// Слишком сложно
public class TypeAnnotationsAttribute extends Attribute {
	
	private final @Immutable List<TypeAnnotation> parametersAnnotations;
	
	public TypeAnnotationsAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		this(name, length, in.readImmutableList(() -> new TypeAnnotation(in, pool)));
	}
	
	public TypeAnnotationsAttribute(String name, int length, @Immutable List<TypeAnnotation> parametersAnnotations) {
		super(name, length);
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
		
		public static final TypeAnnotationsAttribute
				VISIBLE = new EmptyParameterAnnotationsAttribute("RuntimeVisibleParameterAnnotations"),
				INVISIBLE = new EmptyParameterAnnotationsAttribute("RuntimeInvisibleParameterAnnotations");
		
		public EmptyParameterAnnotationsAttribute(String name) {
			super(name, 0, Collections.emptyList());
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
		classinfo.addImportsFor(parametersAnnotations);
	}
	
	public void write(StringifyOutputStream out, ClassInfo classinfo, int index) {
		if(index < parametersAnnotations.size() && parametersAnnotations.get(index) != null)
			parametersAnnotations.get(index).writeTo(out, classinfo);
	}
	
	
	public static class TypeAnnotation implements StringifyWritable<ClassInfo>, Importable {
		
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
