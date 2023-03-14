package x590.jdecompiler.main.performing;

import java.io.OutputStream;

import x590.jdecompiler.JavaClass;
import x590.jdecompiler.io.DisassemblingOutputStream;

public class DisassemblingPerforming extends DecodingPerforming<DisassemblingOutputStream> {
	
	public DisassemblingPerforming(boolean separateOutputStream) {
		super(separateOutputStream);
	}
	
	@Override
	protected DisassemblingOutputStream createOutputStream(OutputStream out) {
		return new DisassemblingOutputStream(out);
	}
	
	@Override
	public void perform(JavaClass clazz) {}
	
	@Override
	public void doWrite(JavaClass clazz) {
		super.doWrite(clazz);
		clazz.writeDisassembled(out);
	}
}
