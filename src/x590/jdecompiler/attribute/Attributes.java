package x590.jdecompiler.attribute;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.Importable;
import x590.jdecompiler.JavaSerializable;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.exception.AttributeNotFoundException;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.ExtendedDataOutputStream;
import x590.util.Logger;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

/**
 * Представляет набор атрибутов. Хранит только уникальные атрибуты
 */
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
	
	/**
	 * Читает атрибуты из потока
	 */
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
	public void serialize(ExtendedDataOutputStream out) {
		out.writeAll(attributes);
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImportsFor(attributes);
	}
	
	
	
	public <A extends Attribute> A get(AttributeType<A> type) {
		return getOrThrow(type, () -> new AttributeNotFoundException(type.getName()));
	}
	
	@SuppressWarnings("unchecked")
	public <A extends Attribute> @Nullable A getNullable(AttributeType<A> type) {
		return (A)attributeByName.get(type.getName());
	}
	
	@SuppressWarnings("unchecked")
	public <A extends Attribute> A getOrDefault(AttributeType<A> type, A defaultValue) {
		return (A)attributeByName.getOrDefault(type.getName(), defaultValue);
	}
	
	public <A extends Attribute, T extends Throwable> A getOrThrow(AttributeType<A> type, T throwable) throws T {
		return getOrThrow(type, () -> throwable);
	}
	
	public <A extends Attribute, T extends Throwable> A getOrThrow(AttributeType<A> type, Supplier<T> exceptionSupplier) throws T {
		
		@SuppressWarnings("unchecked")
		A attribute = (A)attributeByName.get(type.getName());
		
		if(attribute != null)
			return attribute;
		
		throw exceptionSupplier.get();
	}
	
	public boolean has(AttributeType<?> type) {
		return attributeByName.containsKey(type.getName());
	}
}
