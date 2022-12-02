package x590.javaclass.exception;

import x590.javaclass.NameDisassemblingException;
import x590.javaclass.io.ExtendedStringReader;

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