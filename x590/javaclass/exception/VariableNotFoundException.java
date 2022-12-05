package x590.javaclass.exception;

public class VariableNotFoundException extends DecompilationException {
	
	private static final long serialVersionUID = 7564801037667031273L;
	
	public VariableNotFoundException() {
		super();
	}
	
	public VariableNotFoundException(String message) {
		super(message);
	}
}