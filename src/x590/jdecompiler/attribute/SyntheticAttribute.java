package x590.jdecompiler.attribute;

public final class SyntheticAttribute extends EmptyAttribute {
	
	private static final SyntheticAttribute INSTANCE = new SyntheticAttribute();
	
	private SyntheticAttribute() {
		super(AttributeNames.SYNTHETIC);
	}
	
	public static SyntheticAttribute get(String name, int length) {
		checkLength(name, length);
		return INSTANCE;
	}
}
