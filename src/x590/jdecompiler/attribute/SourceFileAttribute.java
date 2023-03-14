package x590.jdecompiler.attribute;

import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;

public class SourceFileAttribute extends Attribute {
	
	private final String sourceFileName;
	
	SourceFileAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(name, length);
		checkLength(name, length, 2);
		this.sourceFileName = pool.getUtf8String(in.readUnsignedShort());
	}
	
	public String getSourceFileName() {
		return sourceFileName;
	}
}
