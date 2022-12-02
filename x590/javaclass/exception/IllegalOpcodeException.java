package x590.javaclass.exception;

public class IllegalOpcodeException extends ClassFormatException {

	private static final long serialVersionUID = 4282824108768458025L;
	
	public IllegalOpcodeException() {
		super();
	}
	
	public IllegalOpcodeException(String message) {
		super(message);
	}
	
	public IllegalOpcodeException(Throwable cause) {
		super(cause);
	}
	
	public IllegalOpcodeException(String message, Throwable cause) {
		super(message, cause);
	}
}