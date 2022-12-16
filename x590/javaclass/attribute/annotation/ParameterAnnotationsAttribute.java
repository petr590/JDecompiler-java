package x590.javaclass.attribute.annotation;

import java.util.Arrays;

import x590.javaclass.ClassInfo;
import x590.javaclass.StringWritableAndImportable;
import x590.javaclass.attribute.Attribute;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.util.Util;

public class ParameterAnnotationsAttribute extends Attribute {
	
	private static final ParameterAnnotations[] EMPTY_PARAMETERS = {};
	
	public static final ParameterAnnotationsAttribute
			EMPTY_VISIBLE = new ParameterAnnotationsAttribute("RuntimeVisibleParameterAnnotations"),
			EMPTY_INVISIBLE = new ParameterAnnotationsAttribute("RuntimeInvisibleParameterAnnotations");
	
	private final ParameterAnnotations[] parametersAnnotations;
	
	public ParameterAnnotationsAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		this(nameIndex, name, length, in.readArray(ParameterAnnotations[]::new, () -> new ParameterAnnotations(in, pool)));
	}
	
	private ParameterAnnotationsAttribute(String name) {
		this(0, name, 0, EMPTY_PARAMETERS);
	}
	
	public ParameterAnnotationsAttribute(int nameIndex, String name, int length, ParameterAnnotations[] parametersAnnotations) {
		super(nameIndex, name, length);
		this.parametersAnnotations = parametersAnnotations;
	}
	
	
	public static ParameterAnnotationsAttribute emptyVisible() {
		return EMPTY_VISIBLE;
	}
	
	public static ParameterAnnotationsAttribute emptyInvisible() {
		return EMPTY_INVISIBLE;
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		Util.forEach(parametersAnnotations, parameterAnnotations -> parameterAnnotations.addImports(classinfo));
	}
	
	public void write(StringifyOutputStream out, ClassInfo classinfo, int index) {
		if(index < parametersAnnotations.length && parametersAnnotations[index] != null)
			parametersAnnotations[index].writeTo(out, classinfo);
	}
	
	
	public static class ParameterAnnotations implements StringWritableAndImportable {
		
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