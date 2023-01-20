package x590.jdecompiler.exception;

public class DecompilationException extends RuntimeException {
	
	private static final long serialVersionUID = -3206146674439197384L;
	
	public DecompilationException() {
		super();
	}
	
	public DecompilationException(String message) {
		super(message);
	}
	
	public DecompilationException(Throwable cause) {
		super(cause);
	}
	
	public DecompilationException(String message, Throwable cause) {
		super(message, cause);
	}
}
