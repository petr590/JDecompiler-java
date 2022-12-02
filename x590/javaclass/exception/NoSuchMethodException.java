package x590.javaclass.exception;

import x590.javaclass.MethodDescriptor;

public class NoSuchMethodException extends NoSuchClassMemberException {
	
	private static final long serialVersionUID = -6567642506652252778L;
	
	public NoSuchMethodException(String message) {
		super(message);
	}
	
	public NoSuchMethodException(MethodDescriptor descriptor) {
		super(descriptor);
	}
}