package x590.jdecompiler.exception;

public class InvalidOpcodeException extends ClassFormatException {
	
	private static final long serialVersionUID = 4282824108768458025L;
	
	public InvalidOpcodeException() {
		super();
	}
	
	public InvalidOpcodeException(String message) {
		super(message);
	}
	
	public InvalidOpcodeException(Throwable cause) {
		super(cause);
	}
	
	public InvalidOpcodeException(String message, Throwable cause) {
		super(message, cause);
	}
}
