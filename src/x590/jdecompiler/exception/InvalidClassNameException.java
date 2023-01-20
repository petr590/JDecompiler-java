package x590.jdecompiler.exception;

import x590.jdecompiler.io.ExtendedStringReader;

public class InvalidClassNameException extends InvalidTypeNameException {
	
	private static final long serialVersionUID = -4562573452176773150L;
	
	public InvalidClassNameException(String encodedName) {
		super(encodedName);
	}
	
	public InvalidClassNameException(String encodedName, int pos) {
		super(encodedName, pos);
	}
	
	public InvalidClassNameException(ExtendedStringReader in) {
		super(in);
	}
	
	public InvalidClassNameException(ExtendedStringReader in, int pos) {
		super(in, pos);
	}
}
