package x590.javaclass.attribute;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import x590.javaclass.ClassInfo;
import x590.javaclass.Importable;
import x590.javaclass.JavaSerializable;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.util.Util;

public class Attributes implements JavaSerializable, Importable {
	
	public static enum Location {
		CLASS, FIELD, METHOD, CODE_ATTRIBUTE, OTHER
	}
	
	
	private static final Attributes EMPTY = new Attributes();
	
	private final Attribute[] attributes;
	private final Map<String, Attribute> attributeByName;
	
	private Attributes() {
		this.attributes = new Attribute[] {};
		this.attributeByName = Collections.emptyMap();
	}
	
	public Attributes(ExtendedDataInputStream in, ConstantPool pool, Location location) {
		int length = in.readUnsignedShort();
		var attributes = this.attributes = new Attribute[length];
		var attributeByName = this.attributeByName = new HashMap<>(length);
		
		for(int i = 0; i < length; i++) {
			Attribute attribute = Attribute.read(in, pool, location);
			attributes[i] = attribute;
			attributeByName.put(attribute.name, attribute);
		}
	}
	
	
	public static Attributes empty() {
		return EMPTY;
	}
	
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeShort(attributes.length);
		for(Attribute attribute : attributes)
			attribute.serialize(out);
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		Util.forEach(attributes, attribute -> attribute.addImports(classinfo));
	}
	
	
	
	@SuppressWarnings("unchecked")
	public <T extends Attribute> T get(String name) {
		return (T)attributeByName.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Attribute> T getOrDefault(String name, T defaultValue) {
		return (T)attributeByName.getOrDefault(name, defaultValue);
	}
	
	public boolean has(String name) {
		return attributeByName.containsKey(name);
	}
}