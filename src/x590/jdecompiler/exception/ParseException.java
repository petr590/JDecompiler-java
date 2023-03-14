package x590.jdecompiler.exception;

public class ParseException extends RuntimeException {
	
	private static final long serialVersionUID = -1811782000015693429L;
	
	public ParseException() {
		super();
	}
	
	public ParseException(String message) {
		super(message);
	}
	
	public ParseException(Throwable cause) {
		super(cause);
	}
	
	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}
}
