package x590.jdecompiler.util;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.writable.SameDisassemblingStringifyWritable;

public abstract class AbstractWhitespaceStringBuilder implements IWhitespaceStringBuilder, SameDisassemblingStringifyWritable<ClassInfo> {
	
	protected boolean printTrailingSpace;
	
	public AbstractWhitespaceStringBuilder() {}
	
	public AbstractWhitespaceStringBuilder(boolean printTrailingSpace) {
		this.printTrailingSpace = printTrailingSpace;
	}
	
	@Override
	public abstract String toString();
	
	@Override
	public AbstractWhitespaceStringBuilder printTrailingSpace(boolean print) {
		this.printTrailingSpace = print;
		return this;
	}
}
