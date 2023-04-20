package x590.jdecompiler.writable;

import x590.jdecompiler.io.StringifyOutputStream;

/**
 * Описывает объект, который можно записать в {@link StringifyOutputStream}
 */
public interface StringifyWritable<T> extends Writable<T> {
	public void writeTo(StringifyOutputStream out, T param);
}
