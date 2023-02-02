package x590.jdecompiler.attribute.annotation;

import java.util.Arrays;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.Importable;
import x590.jdecompiler.StringWritable;
import x590.jdecompiler.attribute.Attribute;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.util.ArrayUtil;

public class ParameterAnnotationsAttribute extends Attribute {
	
	private final ParameterAnnotations[] parametersAnnotations;
	
	public ParameterAnnotationsAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		this(nameIndex, name, length, in.readArray(ParameterAnnotations[]::new, () -> new ParameterAnnotations(in, pool)));
	}
	
	public ParameterAnnotationsAttribute(int nameIndex, String name, int length, ParameterAnnotations[] parametersAnnotations) {
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
		
		private static final ParameterAnnotations[] EMPTY_PARAMETERS = {};
		
		public static final ParameterAnnotationsAttribute
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
	
	
	public static class ParameterAnnotations implements StringWritable<ClassInfo>, Importable {
		
		private final Annotation[] annotations;
		
		public ParameterAnnotations(ExtendedDataInputStream in, ConstantPool pool) {
			this.annotations = in.readArray(Annotation[]::new, () -> new Annotation(in, pool));
		}
		
		@Override
		public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
			Arrays.stream(annotations).forEachOrdered(annotation -> out.writesp(annotation, classinfo));
		}
	}
}
