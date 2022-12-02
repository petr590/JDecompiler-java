package x590.javaclass;

import x590.javaclass.io.StringifyOutputStream;

public interface StringWritable {
	
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo);
}