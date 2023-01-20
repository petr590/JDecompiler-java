package x590.jdecompiler.exception;

public class DisassemblingException extends RuntimeException {
	
	private static final long serialVersionUID = -1857134960093430464L;
	
	public DisassemblingException() {
		super();
	}
	
	public DisassemblingException(String message) {
		super(message);
	}
	
	public DisassemblingException(Throwable cause) {
		super(cause);
	}
	
	public DisassemblingException(String message, Throwable cause) {
		super(message, cause);
	}
}
