package x590.jdecompiler.attribute;

import x590.jdecompiler.exception.DisassemblingException;

public class EmptyAttribute extends Attribute {
	
	protected EmptyAttribute(String name) {
		super(0, name, 0);
	}
	
	protected static void checkLength(String name, int length) {
		if(length != 0)
			throw new DisassemblingException("Length of the \"" + name + "\" attribute must be 0, got " + length);
	}
}
