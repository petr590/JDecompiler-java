package x590.jdecompiler.main.performing;

import java.io.OutputStream;

import x590.jdecompiler.clazz.JavaClass;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.Config;

public class DecompilingPerforming extends DecodingPerforming<StringifyOutputStream> {
	
	public DecompilingPerforming(Config config) {
		super(config);
	}
	
	@Override
	protected StringifyOutputStream createOutputStream(OutputStream out) {
		return new StringifyOutputStream(out);
	}
	
	@Override
	public void perform(JavaClass clazz) {
		clazz.decompile();
	}

	@Override
	public void afterPerforming(JavaClass clazz) {
		clazz.afterDecompilation();
		clazz.resolveImports();
	}
	
	@Override
	public void doWrite(JavaClass clazz) {
		super.writeSeparator();
		clazz.writeTo(out);
		super.writeSeparator();
	}
}
