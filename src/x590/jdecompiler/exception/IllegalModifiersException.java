package x590.jdecompiler.exception;

import x590.jdecompiler.JavaClassElement;
import x590.jdecompiler.modifiers.Modifiers;

public class IllegalModifiersException extends DecompilationException {
	
	private static final long serialVersionUID = 5025033872617565868L;
	
	public IllegalModifiersException(String message) {
		super(message);
	}
	
	public IllegalModifiersException(Modifiers modifiers) {
		super(modifiers.toHexWithPrefix());
	}
	
	public IllegalModifiersException(JavaClassElement element, Modifiers modifiers) {
		super("in " + element.getModifiersTarget() + ": " + modifiers.toHexWithPrefix());
	}
	
	public IllegalModifiersException(JavaClassElement element, Modifiers modifiers, String message) {
		super("in " + element.getModifiersTarget() + ": " + modifiers.toHexWithPrefix() + ": " + message);
	}
}
