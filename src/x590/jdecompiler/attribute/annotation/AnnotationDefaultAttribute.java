package x590.jdecompiler.attribute.annotation;

import x590.jdecompiler.Importable;
import x590.jdecompiler.attribute.Attribute;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.writable.StringifyWritable;

public final class AnnotationDefaultAttribute extends Attribute implements StringifyWritable<ClassInfo>, Importable {
	
	private final ElementValue value;
	
	public AnnotationDefaultAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(name, length);
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
