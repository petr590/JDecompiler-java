package x590.jdecompiler.attribute.annotation;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.StringWritableAndImportable;
import x590.jdecompiler.attribute.Attribute;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;

public final class AnnotationDefaultAttribute extends Attribute implements StringWritableAndImportable {
	
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
