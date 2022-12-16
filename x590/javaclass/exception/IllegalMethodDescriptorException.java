package x590.javaclass.exception;

import x590.javaclass.NameDisassemblingException;
import x590.javaclass.io.ExtendedStringReader;

public class IllegalMethodDescriptorException extends NameDisassemblingException {
	
	private static final long serialVersionUID = -3425457614642349478L;
	
	public IllegalMethodDescriptorException(String encodedName) {
		super(encodedName);
	}
	
	public IllegalMethodDescriptorException(String encodedName, int pos) {
		super(encodedName, pos);
	}
	
	public IllegalMethodDescriptorException(ExtendedStringReader in) {
		super(in);
	}
	
	public IllegalMethodDescriptorException(ExtendedStringReader in, int pos) {
		super(in, pos);
	}
}