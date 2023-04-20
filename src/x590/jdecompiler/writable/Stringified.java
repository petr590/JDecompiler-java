package x590.jdecompiler.writable;

import x590.jdecompiler.io.StringifyOutputStream;

/**
 * Описывает объект, который можно привести к строке с помощью параметра
 */
public interface Stringified<T> extends StringifyWritable<T> {
	
	public String toString(T param);
	
	@Override
	public default void writeTo(StringifyOutputStream out, T param) {
		out.write(this.toString(param));
	}
}
