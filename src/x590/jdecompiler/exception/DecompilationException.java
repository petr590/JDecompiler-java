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
	
	private static String exceptionToString(Throwable throwable) {
		String className = throwable.getClass().getSimpleName();
		String message = throwable.getLocalizedMessage();
		return message == null ? className : className + ": " + message;
	}
	
	public String getFullMessage() {
		String message = exceptionToString(this);
		Throwable cause = getCause();
		return cause == null ? message : message + "; Caused by: " + exceptionToString(cause);
	}
}
