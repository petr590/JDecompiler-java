package x590.javaclass.exception;

public class ClassFormatException extends DisassemblingException {

	private static final long serialVersionUID = 187754517612972423L;
	
	public ClassFormatException() {
		super();
	}
	
	public ClassFormatException(String message) {
		super(message);
	}
	
	public ClassFormatException(Throwable cause) {
		super(cause);
	}
	
	public ClassFormatException(String message, Throwable cause) {
		super(message, cause);
	}
}