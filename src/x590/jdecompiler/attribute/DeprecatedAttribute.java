package x590.jdecompiler.attribute;

public class DeprecatedAttribute extends EmptyAttribute {
	
	private static final DeprecatedAttribute INSTANCE = new DeprecatedAttribute();
	
	private DeprecatedAttribute() {
		super("Deprecated");
	}
	
	public static DeprecatedAttribute get(int nameIndex, String name, int length) {
		checkLength(name, length);
		return INSTANCE;
	}
}
