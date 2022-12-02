package x590.javaclass.attribute.annotation;

import x590.javaclass.ClassInfo;
import x590.javaclass.Stringified;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.io.ExtendedDataInputStream;

public class Element implements Stringified {
	
	private final String name;
	private final ElementValue value;
	
	protected Element(ExtendedDataInputStream in, ConstantPool pool) {
		this.name = pool.getUtf8String(in.readUnsignedShort());
		this.value = ElementValue.read(in, pool);
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return name + "=" + value.toString(classinfo);
	}
}
