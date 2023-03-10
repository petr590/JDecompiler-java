package x590.jdecompiler;

import x590.jdecompiler.io.DisassemblingOutputStream;

/**
 * Описывает объект, который можно записать в {@link DisassemblingOutputStream}
 */
public interface DisassemblingWritable<T> extends Writable<T> {
	public void writeDisassembled(DisassemblingOutputStream out, T param);
}
