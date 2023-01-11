package x590.jdecompiler;

import x590.jdecompiler.io.StringifyOutputStream;

/**
 * Описывает объект, который можно записать в StringifyOutputStream
 */
public interface StringWritable {
	
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo);
}
