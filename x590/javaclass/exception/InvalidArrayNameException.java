package x590.javaclass.exception;

import x590.javaclass.io.ExtendedStringReader;

public class InvalidArrayNameException extends InvalidTypeNameException {
	
	private static final long serialVersionUID = -2261099827704297685L;
	
	public InvalidArrayNameException(String encodedName) {
		super(encodedName);
	}
	
	public InvalidArrayNameException(String encodedName, int pos) {
		super(encodedName, pos);
	}
	
	public InvalidArrayNameException(ExtendedStringReader in) {
		super(in);
	}
	
	public InvalidArrayNameException(ExtendedStringReader in, int pos) {
		super(in, pos);
	}
}