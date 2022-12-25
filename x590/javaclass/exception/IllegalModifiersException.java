package x590.javaclass.exception;

import x590.javaclass.Modifiers;

public class IllegalModifiersException extends DecompilationException {
	
	private static final long serialVersionUID = 5025033872617565868L;
	
	public IllegalModifiersException(Modifiers modifiers) {
		super(modifiers.toHexWithPrefix());
	}
	
	public IllegalModifiersException(String message) {
		super(message);
	}
}