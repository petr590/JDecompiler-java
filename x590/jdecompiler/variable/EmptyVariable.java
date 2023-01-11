package x590.jdecompiler.variable;

/**
 * Используется для замены {@literal null}, чтобы не проверять на {@literal null} везде.
 */
public class EmptyVariable extends EmptyableVariable {
	
	public static final EmptyVariable INSTANCE = new EmptyVariable();
	
	
	private EmptyVariable() {}
	
	
	@Override
	public boolean isEmpty() {
		return true;
	}
	
	@Override
	public boolean hasName() {
		return false;
	}
	
	@Override
	public void assignName() {}
	
	@Override
	public String getName() {
		throw new UnsupportedOperationException("Empty variable has not name");
	}
	
	
	@Override
	public void reduceType() {}
	
	
	@Override
	public Variable notEmpty() {
		throw new UnsupportedOperationException("Variable is empty");
	}
}
