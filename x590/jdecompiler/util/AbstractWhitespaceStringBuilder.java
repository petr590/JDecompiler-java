package x590.jdecompiler.util;

public abstract class AbstractWhitespaceStringBuilder implements IWhitespaceStringBuilder {
	
	protected boolean printTrailingSpace;
	
	public AbstractWhitespaceStringBuilder() {}
	
	public AbstractWhitespaceStringBuilder(boolean printTrailingSpace) {
		this.printTrailingSpace = printTrailingSpace;
	}
	
	@Override
	public abstract String toString();
	
	@Override
	public AbstractWhitespaceStringBuilder printTrailingSpace() {
		this.printTrailingSpace = true;
		return this;
	}
}
