package x590.javaclass.exception;

import x590.javaclass.type.Type;
import x590.javaclass.type.TypeSize;

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
		super("Required " + requiredSize + ", got " + size + " of type " + type);
	}
}