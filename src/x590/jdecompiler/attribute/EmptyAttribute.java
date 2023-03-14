package x590.jdecompiler.attribute;

public class EmptyAttribute extends Attribute {
	
	protected EmptyAttribute(String name) {
		super(name, 0);
	}
	
	protected static void checkLength(String name, int length) {
		checkLength(name, length, 0);
	}
}
