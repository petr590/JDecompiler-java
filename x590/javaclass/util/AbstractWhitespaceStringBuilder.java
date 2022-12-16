package x590.javaclass.util;

public abstract class AbstractWhitespaceStringBuilder<SB extends AbstractWhitespaceStringBuilder<SB>> implements IWhitespaceStringBuilder {
	
	protected boolean printTrailingSpace;
	
	public AbstractWhitespaceStringBuilder() {}
	
	public AbstractWhitespaceStringBuilder(boolean printTrailingSpace) {
		this.printTrailingSpace = printTrailingSpace;
	}
	
	@Override
	public abstract String toString();
	
	@Override
	@SuppressWarnings("unchecked")
	public SB printTrailingSpace() {
		this.printTrailingSpace = true;
		return (SB)this;
	}
}