package x590.javaclass.exception;

import x590.javaclass.Descriptor;

public abstract class NoSuchClassMemberException extends DecompilationException {
	
	private static final long serialVersionUID = 814234522525746027L;
	
	public NoSuchClassMemberException(String message) {
		super(message);
	}
	
	public NoSuchClassMemberException(Descriptor descriptor) {
		super(descriptor.toString());
	}
}