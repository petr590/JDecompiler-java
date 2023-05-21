package x590.jdecompiler.attribute.annotation;

import x590.jdecompiler.Importable;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.writable.StringifyWritable;

public final class Element implements StringifyWritable<ClassInfo>, Importable {
	
	private final String name;
	private final ElementValue value;
	
	protected Element(ExtendedDataInputStream in, ConstantPool pool) {
		this.name = pool.getUtf8String(in.readUnsignedShort());
		this.value = ElementValue.read(in, pool);
	}
	
	private Element(String name, ElementValue value) {
		this.name = name;
		this.value = value;
	}
	
	public static Element fromUnknownValue(String name, Object value) {
		return new Element(name, ElementValue.fromUnknownValue(value));
	}
	
	
	public String getName() {
		return name;
	}
	
	public ElementValue getValue() {
		return value;
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.print(name).print(" = ").print(value, classinfo);
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
		return this == other || name.equals(other.name) && value.equals(other.value);
	}
}
