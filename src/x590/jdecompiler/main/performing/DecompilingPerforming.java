package x590.jdecompiler.main.performing;

import java.io.OutputStream;

import x590.jdecompiler.clazz.JavaClass;
import x590.jdecompiler.io.StringifyOutputStream;

public class DecompilingPerforming extends DecodingPerforming<StringifyOutputStream> {
	
	public DecompilingPerforming(boolean separateOutputStream) {
		super(separateOutputStream);
	}
	
	@Override
	protected StringifyOutputStream createOutputStream(OutputStream out) {
		return new StringifyOutputStream(out);
	}
	
	@Override
	public void perform(JavaClass clazz) {
		clazz.decompile();
		clazz.resolveImports();
	}
	
	@Override
	public void doWrite(JavaClass clazz) {
		super.writeSeparator();
		clazz.writeTo(out);
		super.writeSeparator();
	}
}
