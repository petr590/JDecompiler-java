package x590.javaclass.exception;

import x590.javaclass.io.ExtendedStringReader;

public class InvalidSignatureException extends InvalidTypeNameException {
	
	private static final long serialVersionUID = -1561793936986160127L;
	
	public InvalidSignatureException(String encodedName) {
		super(encodedName);
	}
	
	public InvalidSignatureException(String encodedName, int pos) {
		super(encodedName, pos);
	}
	
	public InvalidSignatureException(ExtendedStringReader in) {
		super(in);
	}
	
	public InvalidSignatureException(ExtendedStringReader in, int pos) {
		super(in, pos);
	}
}