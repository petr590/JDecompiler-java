package x590.jdecompiler.exception;

import x590.jdecompiler.io.ExtendedStringReader;

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
