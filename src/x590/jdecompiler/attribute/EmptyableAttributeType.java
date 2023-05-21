package x590.jdecompiler.attribute;

import java.util.Set;

import x590.jdecompiler.attribute.Attributes.Location;

/**
 * Содержит пустой атрибут
 */
public class EmptyableAttributeType<A extends Attribute> extends AttributeType<A> {
	
	private final A emptyAttribute;
	
	protected EmptyableAttributeType(Location location, String name, AttributeReader<A> reader, A emptyAttribute) {
		super(location, name, reader);
		this.emptyAttribute = emptyAttribute;
	}
	
	protected EmptyableAttributeType(Set<Location> locations, String name, AttributeReader<A> reader, A emptyAttribute) {
		super(locations, name, reader);
		this.emptyAttribute = emptyAttribute;
	}
	
	public A getEmptyAttribute() {
		return emptyAttribute;
	}
}
