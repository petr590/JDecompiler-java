package x590.jdecompiler.exception;

import x590.jdecompiler.FieldDescriptor;

public class NoSuchFieldException extends NoSuchClassMemberException {
	
	private static final long serialVersionUID = 3224048661190569290L;
	
	public NoSuchFieldException(String message) {
		super(message);
	}
	
	public NoSuchFieldException(FieldDescriptor descriptor) {
		super(descriptor);
	}
}
