package x590.jdecompiler;

import x590.jdecompiler.io.StringifyOutputStream;

/**
 * Описывает объект, который можно записать в StringifyOutputStream
 */
public interface StringWritableWithTwoArgs<T, U> {
	public void writeTo(StringifyOutputStream out, T param1, U param2);
}
