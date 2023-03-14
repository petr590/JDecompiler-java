package x590.jdecompiler;


import x590.jdecompiler.io.ExtendedDataOutputStream;

public interface JavaSerializable {
	public void serialize(ExtendedDataOutputStream out);
}
