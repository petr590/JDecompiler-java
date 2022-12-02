package x590.javaclass.attribute;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import x590.javaclass.JavaSerializable;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.io.ExtendedDataInputStream;

public class Attributes implements JavaSerializable {

	private final Attribute[] attributes;
	private final Map<String, Attribute> attributeByName;
	
	public Attributes(ExtendedDataInputStream in, ConstantPool pool) {
		int length = in.readUnsignedShort();
		var attributes = this.attributes = new Attribute[length];
		var attributeByName = this.attributeByName = new HashMap<>(length);

		for(int i = 0; i < length; i++) {
			Attribute attribute = Attribute.read(in, pool);
			attributes[i] = attribute;
			attributeByName.put(attribute.name, attribute);
		}
	}
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeShort(attributes.length);
		for(Attribute attribute : attributes)
			attribute.serialize(out);
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
