package x590.jdecompiler.exception;

import x590.jdecompiler.type.ICastingKind;
import x590.jdecompiler.type.Type;

public class IncopatibleTypesException extends DecompilationException {
	
	private static final long serialVersionUID = -4152058291198634873L;
	
	public IncopatibleTypesException() {
		super();
	}
	
	public IncopatibleTypesException(String message) {
		super(message);
	}
	
	public IncopatibleTypesException(Throwable cause) {
		super(cause);
	}
	
	public IncopatibleTypesException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IncopatibleTypesException(Type type1, Type type2, ICastingKind kind) {
		super("Incopatible types in " + kind.lowerCaseName() + " conversation: " +
				type1.toString() + " (" + type1.getClass().getSimpleName() + ") and " +
				type2.toString() + " (" + type2.getClass().getSimpleName() + ')');
	}
}
