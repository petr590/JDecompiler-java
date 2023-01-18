package x590.jdecompiler.attribute.annotation;

import java.util.Arrays;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.StringWritable;
import x590.jdecompiler.attribute.Attribute;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.util.ArrayUtil;

public final class AnnotationsAttribute extends Attribute implements StringWritable {
	
	private final Annotation[] annotations;
	
	public AnnotationsAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		
		this.annotations = in.readArray(Annotation[]::new, () -> new Annotation(in, pool));
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		ArrayUtil.forEach(annotations, annotation -> annotation.addImports(classinfo));
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		ArrayUtil.forEach(annotations, annotation -> out.printIndent().print(annotation, classinfo).println());
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof AnnotationsAttribute annotationsAttribute && this.equals(annotationsAttribute);
	}
	
	public boolean equals(AnnotationsAttribute other) {
		return this == other || Arrays.equals(annotations, other.annotations);
	}
}
