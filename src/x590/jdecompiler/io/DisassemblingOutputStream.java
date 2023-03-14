package x590.jdecompiler.io;

import java.io.OutputStream;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import x590.jdecompiler.DisassemblingWritable;
import x590.jdecompiler.Writable;
import x590.util.LoopUtil;
import x590.util.annotation.Nullable;
import x590.util.function.TriConsumer;

public class DisassemblingOutputStream extends ExtendedOutputStream<DisassemblingOutputStream> {
	
	public DisassemblingOutputStream(OutputStream out) {
		super(out);
	}
	
	
	@Override
	protected <T> void write(Writable<T> writable, T param) {
		((DisassemblingWritable<T>)writable).writeDisassembled(this, param);
	}
	
	
	public <T> DisassemblingOutputStream print(DisassemblingWritable<T> writable, T param) {
		writable.writeDisassembled(this, param);
		return this;
	}
	
	public <T> DisassemblingOutputStream printIfNotNull(@Nullable DisassemblingWritable<T> writable, T param) {
		if(writable != null)
			writable.writeDisassembled(this, param);
		
		return this;
	}
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printUsingFunction(W writable, Consumer<? super W> writer) {
		writer.accept(writable);
		return this;
	}
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printUsingFunction(W writable, BiConsumer<? super W, ? super DisassemblingOutputStream> writer) {
		writer.accept(writable, this);
		return this;
	}
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printUsingFunction(W writable, T param, TriConsumer<? super W, ? super DisassemblingOutputStream, ? super T> writer) {
		writer.accept(writable, this, param);
		return this;
	}
	
	
	public <T> DisassemblingOutputStream println(DisassemblingWritable<T> writable, T param) {
		return print(writable, param).print('\n');
	}
	
	public <T> DisassemblingOutputStream printsp(DisassemblingWritable<T> writable, T param) {
		return print(writable, param).print(' ');
	}
	
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printAll(Collection<? extends W> writables, T param) {
		return printAll(writables, param, ' ');
	}
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printAll(Collection<? extends W> writables, T param, char delimeter) {
		return printAll(writables, param, writable -> write(delimeter));
	}
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printAll(Collection<? extends W> writables, T param, String delimeter) {
		return printAll(writables, param, writable -> write(delimeter));
	}
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printAll(Collection<? extends W> writables, T param, Consumer<? super W> delimeterWriter) {
		return printAllUsingFunction(writables, writable -> writable.writeDisassembled(this, param), delimeterWriter);
	}
	
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printAll(Collection<? extends W> writables, int startIndex, T param) {
		return printAll(writables, startIndex, param, ' ');
	}
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printAll(Collection<? extends W> writables, int startIndex, T param, char delimeter) {
		return printAll(writables, startIndex, param, writable -> write(delimeter));
	}
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printAll(Collection<? extends W> writables, int startIndex, T param, String delimeter) {
		return printAll(writables, startIndex, param, writable -> write(delimeter));
	}
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printAll(Collection<? extends W> writables, int startIndex, T param, Consumer<? super W> delimeterWriter) {
		LoopUtil.forEachExcludingLast(writables, writable -> writable.writeDisassembled(this, param), delimeterWriter, startIndex);
		return this;
	}
	
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printAllUsingFunction(Collection<? extends W> writables, Consumer<? super W> writer) {
		return printAllUsingFunction(writables, writer, ' ');
	}
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printAllUsingFunction(Collection<? extends W> writables, Consumer<? super W> writer, char delimeter) {
		return printAllUsingFunction(writables, writer, writable -> write(delimeter));
	}
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printAllUsingFunction(Collection<? extends W> writables, Consumer<? super W> writer, String delimeter) {
		return printAllUsingFunction(writables, writer, writable -> write(delimeter));
	}
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printAllUsingFunction(Collection<? extends W> writables, Consumer<? super W> writer, Consumer<? super W> delimeterWriter) {
		LoopUtil.forEachExcludingLast(writables, writer, delimeterWriter);
		return this;
	}
	
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printAllUsingFunction(Collection<? extends W> writables, int startIndex, T param) {
		return printAllUsingFunction(writables, startIndex, param, ' ');
	}
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printAllUsingFunction(Collection<? extends W> writables, int startIndex, T param, char delimeter) {
		return printAllUsingFunction(writables, startIndex, param, writable -> write(delimeter));
	}
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printAllUsingFunction(Collection<? extends W> writables, int startIndex, T param, String delimeter) {
		return printAllUsingFunction(writables, startIndex, param, writable -> write(delimeter));
	}
	
	public <T, W extends DisassemblingWritable<T>> DisassemblingOutputStream printAllUsingFunction(Collection<? extends W> writables, int startIndex, T param, Consumer<? super W> delimeterWriter) {
		LoopUtil.forEachExcludingLast(writables, writable -> writable.writeDisassembled(this, param), delimeterWriter, startIndex);
		return this;
	}
	
	
	public <T> DisassemblingOutputStream printEach(Collection<? extends DisassemblingWritable<T>> writables, T param) {
		return printEachUsingFunction(writables, writable -> writable.writeDisassembled(this, param));
	}
	
	public <T> DisassemblingOutputStream printEachUsingFunction(Collection<? extends DisassemblingWritable<T>> writables, Consumer<DisassemblingWritable<T>> writer) {
		writables.forEach(writer);
		return this;
	}
}
