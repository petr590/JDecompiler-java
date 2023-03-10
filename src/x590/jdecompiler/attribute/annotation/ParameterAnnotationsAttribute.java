package x590.jdecompiler.attribute.annotation;

import java.util.Arrays;
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

public class ParameterAnnotationsAttribute extends Attribute {
	
	private final @Immutable List<ParameterAnnotations> parametersAnnotations;
	
	public ParameterAnnotationsAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		this(nameIndex, name, length, in.readImmutableList(() -> new ParameterAnnotations(in, pool)));
	}
	
	public ParameterAnnotationsAttribute(int nameIndex, String name, int length, @Immutable List<ParameterAnnotations> parametersAnnotations) {
		super(nameIndex, name, length);
		this.parametersAnnotations = parametersAnnotations;
	}
	
	
	public static ParameterAnnotationsAttribute emptyVisible() {
		return EmptyParameterAnnotationsAttribute.VISIBLE;
	}
	
	public static ParameterAnnotationsAttribute emptyInvisible() {
		return EmptyParameterAnnotationsAttribute.INVISIBLE;
	}
	
	
	public boolean isEmpty() {
		return false;
	}
	
	
	public static final class EmptyParameterAnnotationsAttribute extends ParameterAnnotationsAttribute {
		
		public static final ParameterAnnotationsAttribute
				VISIBLE = new EmptyParameterAnnotationsAttribute("RuntimeVisibleParameterAnnotations"),
				INVISIBLE = new EmptyParameterAnnotationsAttribute("RuntimeInvisibleParameterAnnotations");
		
		public EmptyParameterAnnotationsAttribute(String name) {
			super(0, name, 0, Collections.emptyList());
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
	
	
	public static class ParameterAnnotations implements StringifyWritable<ClassInfo>, Importable {
		
		private final Annotation[] annotations;
		
		public ParameterAnnotations(ExtendedDataInputStream in, ConstantPool pool) {
			this.annotations = in.readArray(Annotation[]::new, () -> new Annotation(in, pool));
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			Arrays.stream(annotations).forEachOrdered(annotation -> out.printsp(annotation, classinfo));
		}
	}
}
