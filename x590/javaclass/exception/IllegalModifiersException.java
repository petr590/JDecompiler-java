package x590.javaclass.exception;

import x590.javaclass.util.Util;

public class IllegalModifiersException extends DecompilationException {
	
	private static final long serialVersionUID = 5025033872617565868L;
	
	public IllegalModifiersException(int modifiers) {
		super(Util.hex4WithPrefix(modifiers));
	}
	
	public IllegalModifiersException(String message) {
		super(message);
	}
}