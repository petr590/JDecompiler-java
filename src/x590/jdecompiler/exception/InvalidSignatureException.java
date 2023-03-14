package x590.jdecompiler.exception;

import x590.jdecompiler.io.ExtendedStringInputStream;

public class InvalidSignatureException extends InvalidTypeNameException {
	
	private static final long serialVersionUID = -1561793936986160127L;
	
	public InvalidSignatureException(String encodedName) {
		super(encodedName);
	}
	
	public InvalidSignatureException(String encodedName, int pos) {
		super(encodedName, pos);
	}
	
	public InvalidSignatureException(ExtendedStringInputStream in) {
		super(in);
	}
	
	public InvalidSignatureException(ExtendedStringInputStream in, int pos) {
		super(in, pos);
	}
}
