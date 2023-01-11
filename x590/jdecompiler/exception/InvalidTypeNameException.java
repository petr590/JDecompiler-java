package x590.jdecompiler.exception;

import x590.jdecompiler.NameDisassemblingException;
import x590.jdecompiler.io.ExtendedStringReader;

public class InvalidTypeNameException extends NameDisassemblingException {
	
	private static final long serialVersionUID = -5822594539587085905L;
	
	public InvalidTypeNameException(String encodedName) {
		super(encodedName);
	}
	
	public InvalidTypeNameException(String encodedName, int pos) {
		super(encodedName, pos);
	}
	
	public InvalidTypeNameException(ExtendedStringReader in) {
		super(in);
	}
	
	public InvalidTypeNameException(ExtendedStringReader in, int pos) {
		super(in, pos);
	}
}
