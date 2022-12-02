package x590.javaclass.exception;

import x590.javaclass.io.ExtendedStringReader;

public class InvalidClassNameException extends InvalidTypeNameException {

	private static final long serialVersionUID = -4562573452176773150L;

	public InvalidClassNameException(String encodedName) {
		super(encodedName);
	}

	public InvalidClassNameException(String encodedName, int pos) {
		super(encodedName, pos);
	}

	public InvalidClassNameException(ExtendedStringReader in, int pos) {
		super(in, pos);
	}
}