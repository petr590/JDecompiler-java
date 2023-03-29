package x590.jdecompiler.exception;

public class DecompilationException extends RuntimeException {
	
	private static final long serialVersionUID = -3206146674439197384L;
	
	private final int atIndex;
	
	public DecompilationException() {
		super();
		this.atIndex = -1;
	}
	
	public DecompilationException(String message) {
		super(message);
		this.atIndex = -1;
	}
	
	public DecompilationException(Throwable cause) {
		super(cause);
		this.atIndex = -1;
	}
	
	public DecompilationException(String message, Throwable cause) {
		super(message, cause);
		this.atIndex = -1;
	}
	
	public DecompilationException(int atIndex, Throwable cause) {
		super("at index " + atIndex, cause);
		this.atIndex = atIndex;
	}
	
	private static StringBuilder exceptionToStringBuilder(StringBuilder message, Throwable throwable, String atIndexMessage) {
		message.append(throwable.getClass().getSimpleName());
		
		if(atIndexMessage != null) {
			message.append(' ').append(atIndexMessage);
			
		}
		
		String throwableMessage = throwable.getLocalizedMessage();
		if(throwableMessage != null)
			message.append(": ").append(throwableMessage);
		
		return message;
	}
	
	public String getFullMessage() {
		Throwable cause = getCause();
		
		if(atIndex != -1 && cause != null)
			return exceptionToStringBuilder(new StringBuilder(), cause, getLocalizedMessage()).toString();
		
		StringBuilder message = exceptionToStringBuilder(new StringBuilder(), this, null);
		
		if(cause != null) {
			exceptionToStringBuilder(message.append("; Caused by: "), cause, null);
		}
		
		return message.toString();
	}
}
