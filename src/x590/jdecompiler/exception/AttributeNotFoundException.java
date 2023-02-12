package x590.jdecompiler.exception;

public class AttributeNotFoundException extends DecompilationException {
	
	private static final long serialVersionUID = 6412747676049467773L;
	
	
	public AttributeNotFoundException() {
		super();
	}
	
	public AttributeNotFoundException(String message) {
		super(message);
	}
	
	public AttributeNotFoundException(Throwable cause) {
		super(cause);
	}
	
	public AttributeNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
