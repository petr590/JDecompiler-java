package x590.jdecompiler.io;

import java.io.OutputStream;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import x590.jdecompiler.StringifyWritable;
import x590.jdecompiler.Writable;
import x590.jdecompiler.BiStringifyWritable;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.Operation.Associativity;
import x590.util.LoopUtil;
import x590.util.annotation.Nullable;
import x590.util.function.TriConsumer;

public class StringifyOutputStream extends ExtendedOutputStream<StringifyOutputStream> {
	
	public StringifyOutputStream(OutputStream out) {
		super(out);
	}
	
	
	@Override
	protected <T> void write(Writable<T> writable, T param) {
		((StringifyWritable<T>)writable).writeTo(this, param);
	}
	
	
	public <T> StringifyOutputStream print(StringifyWritable<T> writable, T param) {
		writable.writeTo(this, param);
		return this;
	}
	
	public <T> StringifyOutputStream printIfNotNull(@Nullable StringifyWritable<T> writable, T param) {
		if(writable != null)
			writable.writeTo(this, param);
		
		return this;
	}
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printUsingFunction(W writable, Consumer<? super W> writer) {
		writer.accept(writable);
		return this;
	}
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printUsingFunction(W writable, BiConsumer<? super W, ? super StringifyOutputStream> writer) {
		writer.accept(writable, this);
		return this;
	}
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printUsingFunction(W writable, T param, TriConsumer<? super W, ? super StringifyOutputStream, ? super T> writer) {
		writer.accept(writable, this, param);
		return this;
	}
	
	
	public <T> StringifyOutputStream println(StringifyWritable<T> writable, T param) {
		return print(writable, param).print('\n');
	}
	
	public <T> StringifyOutputStream printsp(StringifyWritable<T> writable, T param) {
		return print(writable, param).print(' ');
	}
	
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printAll(Collection<? extends W> writables, T param) {
		return printAll(writables, param, ' ');
	}
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printAll(Collection<? extends W> writables, T param, char delimeter) {
		return printAll(writables, param, writable -> write(delimeter));
	}
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printAll(Collection<? extends W> writables, T param, String delimeter) {
		return printAll(writables, param, writable -> write(delimeter));
	}
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printAll(Collection<? extends W> writables, T param, Consumer<? super W> delimeterWriter) {
		return printAllUsingFunction(writables, writable -> writable.writeTo(this, param), delimeterWriter);
	}
	
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printAll(Collection<? extends W> writables, int startIndex, T param) {
		return printAll(writables, startIndex, param, ' ');
	}
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printAll(Collection<? extends W> writables, int startIndex, T param, char delimeter) {
		return printAll(writables, startIndex, param, writable -> write(delimeter));
	}
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printAll(Collection<? extends W> writables, int startIndex, T param, String delimeter) {
		return printAll(writables, startIndex, param, writable -> write(delimeter));
	}
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printAll(Collection<? extends W> writables, int startIndex, T param, Consumer<? super W> delimeterWriter) {
		LoopUtil.forEachExcludingLast(writables, writable -> writable.writeTo(this, param), delimeterWriter, startIndex);
		return this;
	}
	
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printAllUsingFunction(Collection<? extends W> writables, Consumer<? super W> writer) {
		return printAllUsingFunction(writables, writer, ' ');
	}
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printAllUsingFunction(Collection<? extends W> writables, Consumer<? super W> writer, char delimeter) {
		return printAllUsingFunction(writables, writer, writable -> write(delimeter));
	}
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printAllUsingFunction(Collection<? extends W> writables, Consumer<? super W> writer, String delimeter) {
		return printAllUsingFunction(writables, writer, writable -> write(delimeter));
	}
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printAllUsingFunction(Collection<? extends W> writables, Consumer<? super W> writer, Consumer<? super W> delimeterWriter) {
		LoopUtil.forEachExcludingLast(writables, writer, delimeterWriter);
		return this;
	}
	
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printAllUsingFunction(Collection<? extends W> writables, int startIndex, T param) {
		return printAllUsingFunction(writables, startIndex, param, ' ');
	}
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printAllUsingFunction(Collection<? extends W> writables, int startIndex, T param, char delimeter) {
		return printAllUsingFunction(writables, startIndex, param, writable -> write(delimeter));
	}
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printAllUsingFunction(Collection<? extends W> writables, int startIndex, T param, String delimeter) {
		return printAllUsingFunction(writables, startIndex, param, writable -> write(delimeter));
	}
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printAllUsingFunction(Collection<? extends W> writables, int startIndex, T param, Consumer<? super W> delimeterWriter) {
		LoopUtil.forEachExcludingLast(writables, writable -> writable.writeTo(this, param), delimeterWriter, startIndex);
		return this;
	}
	
	
	public <T> StringifyOutputStream printEach(Collection<? extends StringifyWritable<T>> writables, T param) {
		return printEachUsingFunction(writables, writable -> writable.writeTo(this, param));
	}
	
	public <T, W extends StringifyWritable<T>> StringifyOutputStream printEachUsingFunction(Collection<? extends W> writables, Consumer<? super W> writer) {
		writables.forEach(writer);
		return this;
	}
	
	
	public <T, U> StringifyOutputStream print(BiStringifyWritable<T, U> writable, T param1, U param2) {
		writable.writeTo(this, param1, param2);
		return this;
	}
	
	public <T, U> StringifyOutputStream printIfNotNull(@Nullable BiStringifyWritable<T, U> writable, T param1, U param2) {
		if(writable != null)
			writable.writeTo(this, param1, param2);
		
		return this;
	}
	
	public <T, U, S extends BiStringifyWritable<T, U>> StringifyOutputStream printUsingFunction(S writable, Consumer<? super S> writer) {
		writer.accept(writable);
		return this;
	}
	
	public <T, U> StringifyOutputStream println(BiStringifyWritable<T, U> writable, T param1, U param2) {
		return print(writable, param1, param2).print('\n');
	}
	
	public <T, U> StringifyOutputStream printsp(BiStringifyWritable<T, U> writable, T param1, U param2) {
		return print(writable, param1, param2).print(' ');
	}
	
	
	
	public StringifyOutputStream printPrioritied(Operation thisOperation, Operation otherOperation, StringifyContext param, Associativity associativity) {
		thisOperation.writePrioritied(this, otherOperation, param, associativity);
		return this;
	}
	
	public StringifyOutputStream printPrioritied(Operation thisOperation, Operation otherOperation, StringifyContext param, int thisPriority, Associativity associativity) {
		thisOperation.writePrioritied(this, otherOperation, param, thisPriority, associativity);
		return this;
	}
}
