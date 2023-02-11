package x590.jdecompiler.exception;

public class InstructionFormatException extends ClassFormatException {
	
	private static final long serialVersionUID = -1765290011748352973L;
	
	public InstructionFormatException() {
		super();
	}
	
	public InstructionFormatException(String message) {
		super(message);
	}
	
	public InstructionFormatException(Throwable cause) {
		super(cause);
	}
	
	public InstructionFormatException(String message, Throwable cause) {
		super(message, cause);
	}
}
