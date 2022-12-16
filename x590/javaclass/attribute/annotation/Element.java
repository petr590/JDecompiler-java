package x590.javaclass.attribute.annotation;

import x590.javaclass.ClassInfo;
import x590.javaclass.StringWritableAndImportable;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.StringifyOutputStream;

public class Element implements StringWritableAndImportable {
	
	private final String name;
	private final ElementValue value;
	
	protected Element(ExtendedDataInputStream in, ConstantPool pool) {
		this.name = pool.getUtf8String(in.readUnsignedShort());
		this.value = ElementValue.read(in, pool);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.print(name).print('=').print(value, classinfo);
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		value.addImports(classinfo);
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof Element element && this.equals(element);
	}
	
	public boolean equals(Element other) {
		return this == other || this.name.equals(other.name) && this.value.equals(other.value);
	}
}