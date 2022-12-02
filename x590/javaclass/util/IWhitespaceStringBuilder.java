package x590.javaclass.util;

import x590.javaclass.StringWritable;

public interface IWhitespaceStringBuilder extends StringWritable {
	
	public IWhitespaceStringBuilder append(String str);
	
	public boolean isEmpty();
	
	public String toString();
	
	public IWhitespaceStringBuilder printTrailingSpace();
}