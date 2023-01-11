package x590.jdecompiler;

import x590.jdecompiler.exception.DisassemblingException;
import x590.jdecompiler.io.ExtendedStringReader;

public class NameDisassemblingException extends DisassemblingException {
	
	private static final long serialVersionUID = -5848599648448714466L;
	
	public NameDisassemblingException(String encodedName) {
		super('"' + encodedName + '"');
	}
	
	public NameDisassemblingException(String encodedName, int pos) {
		super('"' + encodedName + '"' + " (at pos " + pos + ")");
	}
	
	public NameDisassemblingException(ExtendedStringReader in) {
		super('"' + in.getAllFromMark() + '"');
	}
	
	public NameDisassemblingException(ExtendedStringReader in, int pos) {
		super('"' + in.getAllFromMark() + '"' + " (at pos " + pos + ")");
	}
}
