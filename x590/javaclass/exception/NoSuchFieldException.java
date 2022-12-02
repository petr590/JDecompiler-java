package x590.javaclass.exception;

import x590.javaclass.FieldDescriptor;

public class NoSuchFieldException extends NoSuchClassMemberException {
	
	private static final long serialVersionUID = 3224048661190569290L;
	
	public NoSuchFieldException(String message) {
		super(message);
	}
	
	public NoSuchFieldException(FieldDescriptor descriptor) {
		super(descriptor);
	}
}