package x590.jdecompiler;

import x590.jdecompiler.io.StringifyOutputStream;

public interface Stringified<T> extends StringWritable<T> {
	
	public String toString(T param);
	
	@Override
	public default void writeTo(StringifyOutputStream out, T param) {
		out.write(this.toString(param));
	}
}
