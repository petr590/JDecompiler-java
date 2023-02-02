package x590.jdecompiler.attribute;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.Importable;
import x590.jdecompiler.JavaSerializable;
import x590.jdecompiler.StringWritable;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.util.Logger;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

@Immutable
public final class Attributes implements JavaSerializable, Importable {
	
	public static enum Location {
		CLASS, FIELD, METHOD, CODE_ATTRIBUTE, OTHER
	}
	
	
	private static final Attributes EMPTY = new Attributes(Collections.emptyList(), Collections.emptyMap());
	
	private final List<Attribute> attributes;
	private final Map<String, Attribute> attributeByName;
	
	private Attributes(List<Attribute> attributes, Map<String, Attribute> attributeByName) {
		this.attributes = attributes;
		this.attributeByName = attributeByName;
	}
	
	public static Attributes read(ExtendedDataInputStream in, ConstantPool pool, Location location) {
		int length = in.readUnsignedShort();
		Map<String, Attribute> attributeByName = new HashMap<>(length);
		
		List<Attribute> attributes = in.readArrayList(length, () -> {
			Attribute attribute = Attribute.read(in, pool, location);
			
			if(attributeByName.put(attribute.getName(), attribute) != null)
				Logger.warning("Found two attributes with the same name \"" + attribute.getName() + "\"");
			
			return attribute;
		});
		
		return new Attributes(attributes, attributeByName);
	}
	
	
	public static Attributes empty() {
		return EMPTY;
	}
	
	
	@Override
	public void serialize(DataOutputStream out) throws IOException {
		out.writeShort(attributes.size());
		for(Attribute attribute : attributes)
			attribute.serialize(out);
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		attributes.forEach(attribute -> attribute.addImports(classinfo));
	}
	
	
	
	@SuppressWarnings("unchecked")
	public <T extends Attribute> @Nullable T get(String name) {
		return (T)attributeByName.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Attribute> T getOrDefault(String name, T defaultValue) {
		return (T)attributeByName.getOrDefault(name, defaultValue);
	}
	
	@SuppressWarnings("unchecked")
	public @Nullable StringWritable<ClassInfo> getAsWritable(String name) {
		return (StringWritable<ClassInfo>)attributeByName.get(name);
	}
	
	public boolean has(String name) {
		return attributeByName.containsKey(name);
	}
}
