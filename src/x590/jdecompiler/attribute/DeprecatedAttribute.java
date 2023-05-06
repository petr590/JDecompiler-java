package x590.jdecompiler.attribute;

public final class DeprecatedAttribute extends EmptyAttribute {
	
	private static final DeprecatedAttribute INSTANCE = new DeprecatedAttribute();
	
	private DeprecatedAttribute() {
		super(AttributeNames.DEPRECATED);
	}
	
	public static DeprecatedAttribute get(String name, int length) {
		checkLength(name, length);
		return INSTANCE;
	}
}
