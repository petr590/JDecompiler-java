package x590.jdecompiler.exception;

import x590.jdecompiler.MethodDescriptor;

public class NoSuchMethodException extends NoSuchClassMemberException {
	
	private static final long serialVersionUID = -6567642506652252778L;
	
	public NoSuchMethodException() {
		super();
	}
	
	public NoSuchMethodException(String message) {
		super(message);
	}
	
	public NoSuchMethodException(MethodDescriptor descriptor) {
		super(descriptor);
	}
}
