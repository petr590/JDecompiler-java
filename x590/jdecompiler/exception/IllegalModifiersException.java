package x590.jdecompiler.exception;

import x590.jdecompiler.modifiers.Modifiers;

public class IllegalModifiersException extends DecompilationException {
	
	private static final long serialVersionUID = 5025033872617565868L;
	
	public IllegalModifiersException(Modifiers modifiers) {
		super(modifiers.toHexWithPrefix());
	}
	
	public IllegalModifiersException(String message) {
		super(message);
	}
}
