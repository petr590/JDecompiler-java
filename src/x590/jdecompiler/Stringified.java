package x590.jdecompiler;

import x590.jdecompiler.io.StringifyOutputStream;

public interface Stringified<T> extends StringifyWritable<T> {
	
	public String toString(T param);
	
	@Override
	public default void writeTo(StringifyOutputStream out, T param) {
		out.write(this.toString(param));
	}
}
