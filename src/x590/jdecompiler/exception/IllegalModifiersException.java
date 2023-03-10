package x590.jdecompiler.exception;

import x590.jdecompiler.modifiers.Modifiers;
import x590.jdecompiler.type.ClassType;

public class IllegalModifiersException extends DecompilationException {
	
	private static final long serialVersionUID = 5025033872617565868L;
	
	public IllegalModifiersException(String message) {
		super(message);
	}
	
	public IllegalModifiersException(Modifiers modifiers) {
		super(modifiers.toHexWithPrefix());
	}
	
	public IllegalModifiersException(ClassType classType, Modifiers modifiers) {
		super("in class " + classType.getName() + ": " + modifiers.toHexWithPrefix());
	}
	
	public IllegalModifiersException(ClassType classType, Modifiers modifiers, String message) {
		super("in class " + classType.getName() + ": " + modifiers.toHexWithPrefix() + ": " + message);
	}
}
