package x590.jdecompiler.util;

import x590.jdecompiler.StringWritable;

public interface IWhitespaceStringBuilder extends StringWritable {
	
	public IWhitespaceStringBuilder append(String str);
	
	public boolean isEmpty();
	
	@Override
	public String toString();
	
	public IWhitespaceStringBuilder printTrailingSpace();
}
