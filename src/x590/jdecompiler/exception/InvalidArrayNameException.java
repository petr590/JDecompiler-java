package x590.jdecompiler.exception;

import x590.jdecompiler.io.ExtendedStringInputStream;

public class InvalidArrayNameException extends InvalidTypeNameException {
	
	private static final long serialVersionUID = -2261099827704297685L;
	
	public InvalidArrayNameException(String encodedName) {
		super(encodedName);
	}
	
	public InvalidArrayNameException(String encodedName, int pos) {
		super(encodedName, pos);
	}
	
	public InvalidArrayNameException(ExtendedStringInputStream in) {
		super(in);
	}
	
	public InvalidArrayNameException(ExtendedStringInputStream in, int pos) {
		super(in, pos);
	}
}
