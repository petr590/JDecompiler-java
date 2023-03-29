package x590.jdecompiler.attribute;

import x590.jdecompiler.attribute.Attributes.Location;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.util.function.ObjIntFunction;

@FunctionalInterface
public interface AttributeReader<A extends Attribute> {
	
	public A readAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool, Location location);
	
	public static <A extends Attribute> AttributeReader<A> convert(ObjIntFunction<String, A> reader) {
		return (name, length, in, pool, location) -> reader.apply(name, length);
	}
	
	@FunctionalInterface
	public interface AttributeReaderIgnoringLocation<A extends Attribute> extends AttributeReader<A> {
		
		public A readAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool);
		
		@Override
		public default A readAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool, Location location) {
			return readAttribute(name, length, in, pool);
		}
	}
}
