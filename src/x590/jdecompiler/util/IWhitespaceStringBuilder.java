package x590.jdecompiler.util;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.writable.DisassemblingStringifyWritable;

public interface IWhitespaceStringBuilder extends DisassemblingStringifyWritable<ClassInfo> {
	
	public IWhitespaceStringBuilder append(String str);
	
	public default IWhitespaceStringBuilder appendIf(boolean condition, String str) {
		return condition ? this.append(str) : this;
	}
	
	public boolean isEmpty();
	
	@Override
	public String toString();
	
	public default IWhitespaceStringBuilder printTrailingSpace() {
		return this.printTrailingSpace(true);
	}
	
	public IWhitespaceStringBuilder printTrailingSpace(boolean print);
}
