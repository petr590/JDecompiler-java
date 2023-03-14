package x590.jdecompiler.exception;

import x590.jdecompiler.io.ExtendedStringInputStream;

public class InvalidClassNameException extends InvalidTypeNameException {
	
	private static final long serialVersionUID = -4562573452176773150L;
	
	public InvalidClassNameException(String encodedName) {
		super(encodedName);
	}
	
	public InvalidClassNameException(String encodedName, int pos) {
		super(encodedName, pos);
	}
	
	public InvalidClassNameException(ExtendedStringInputStream in) {
		super(in);
	}
	
	public InvalidClassNameException(ExtendedStringInputStream in, int pos) {
		super(in, pos);
	}
}
