package x590.jdecompiler.exception;

import x590.jdecompiler.io.ExtendedStringInputStream;

public class NameDisassemblingException extends DisassemblingException {
	
	private static final long serialVersionUID = -5848599648448714466L;
	
	public NameDisassemblingException(String encodedName) {
		super('"' + encodedName + '"');
	}
	
	public NameDisassemblingException(String encodedName, int pos) {
		super('"' + encodedName + '"' + " (at pos " + pos + ")");
	}
	
	public NameDisassemblingException(ExtendedStringInputStream in) {
		super('"' + in.getAllFromMark() + '"');
	}
	
	public NameDisassemblingException(ExtendedStringInputStream in, int pos) {
		super('"' + in.getAllFromMark() + '"' + " (at pos " + pos + ")");
	}
}
