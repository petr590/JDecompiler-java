package x590.jdecompiler.attribute;

import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.util.function.ObjIntFunction;

@FunctionalInterface
public interface AttributeReader<A extends Attribute> {
	public A readAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool);
	
	public static <A extends Attribute> AttributeReader<A> convert(ObjIntFunction<String, A> reader) {
		return (attrName, length, in, pool) -> reader.apply(attrName, length);
	}
}
