package x590.javaclass.attribute.annotation;

import x590.javaclass.ClassInfo;
import x590.javaclass.StringWritableAndImportable;
import x590.javaclass.attribute.Attribute;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.StringifyOutputStream;

public class AnnotationDefaultAttribute extends Attribute implements StringWritableAndImportable {
	
	private final ElementValue value;
	
	public AnnotationDefaultAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		this.value = ElementValue.read(in, pool);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.print(" default ").print(value, classinfo);
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		value.addImports(classinfo);
	}
}