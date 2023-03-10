package x590.jdecompiler.attribute.annotation;

import java.util.List;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.StringifyWritable;
import x590.jdecompiler.attribute.Attribute;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.util.annotation.Immutable;

public final class AnnotationsAttribute extends Attribute implements StringifyWritable<ClassInfo> {
	
	private final @Immutable List<Annotation> annotations;
	
	public AnnotationsAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		
		this.annotations = in.readImmutableList(() -> new Annotation(in, pool));
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImportsFor(annotations);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.printEachUsingFunction(annotations, annotation -> out.printIndent().println(annotation, classinfo));
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof AnnotationsAttribute annotationsAttribute && this.equals(annotationsAttribute);
	}
	
	public boolean equals(AnnotationsAttribute other) {
		return this == other || annotations.equals(other.annotations);
	}
}
