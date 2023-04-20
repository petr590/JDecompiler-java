package x590.jdecompiler.exception;

public class IllegalTypeException extends DecompilationException {
	
	private static final long serialVersionUID = 7747980425322399497L;
	
	public IllegalTypeException() {
		super();
	}
	
	public IllegalTypeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public IllegalTypeException(String message) {
		super(message);
	}
	
	public IllegalTypeException(Throwable cause) {
		super(cause);
	}
}
