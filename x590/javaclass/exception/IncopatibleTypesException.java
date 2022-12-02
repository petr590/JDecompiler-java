package x590.javaclass.exception;

import x590.javaclass.type.Type;

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
	
	public IncopatibleTypesException(Type type1, Type type2) {
		super("Incopatible types: " + type1.toString() + " and " + type2.toString());
	}
}