package x590.jdecompiler.exception;

public class NoSuchConstantException extends DecompilationException {
	
	private static final long serialVersionUID = -4054909876022589550L;
	
	public NoSuchConstantException() {
		super();
	}
	
	public NoSuchConstantException(String message) {
		super(message);
	}
	
	public NoSuchConstantException(Throwable cause) {
		super(cause);
	}
	
	public NoSuchConstantException(String message, Throwable cause) {
		super(message, cause);
	}
}
