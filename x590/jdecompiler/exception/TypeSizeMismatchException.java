package x590.jdecompiler.exception;

import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.TypeSize;

public class TypeSizeMismatchException extends DecompilationException {
	
	private static final long serialVersionUID = 1L;
	
	public TypeSizeMismatchException() {
		super();
	}
	
	public TypeSizeMismatchException(String message) {
		super(message);
	}
	
	public TypeSizeMismatchException(Throwable cause) {
		super(cause);
	}
	
	public TypeSizeMismatchException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TypeSizeMismatchException(TypeSize requiredSize, TypeSize size, Type type) {
		super("Required " + requiredSize + ", got " + size + " from type " + type);
	}
}
