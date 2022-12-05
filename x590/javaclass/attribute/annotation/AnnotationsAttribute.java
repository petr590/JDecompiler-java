package x590.javaclass.attribute.annotation;

import java.util.Arrays;

import x590.javaclass.ClassInfo;
import x590.javaclass.StringWritableAndImportable;
import x590.javaclass.attribute.Attribute;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.util.Util;

public class AnnotationsAttribute extends Attribute implements StringWritableAndImportable {
	
	private final Annotation[] annotations;
	
	public AnnotationsAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		
		int annotationsLength = in.readUnsignedShort();
		var annotations = this.annotations = new Annotation[annotationsLength];
		
		for(int i = 0; i < annotationsLength; i++) {
			annotations[i] = new Annotation(in, pool);
		}
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		Arrays.stream(annotations).forEachOrdered(annotation -> out.printIndent().print(annotation, classinfo).println());
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		Util.forEach(annotations, annotation -> annotation.addImports(classinfo));
	}
}
