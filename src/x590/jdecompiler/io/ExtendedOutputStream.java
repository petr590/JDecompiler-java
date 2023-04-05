package x590.jdecompiler.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import x590.jdecompiler.Writable;
import x590.jdecompiler.main.JDecompiler;
import x590.util.LoopUtil;
import x590.util.annotation.Nullable;
import x590.util.function.TriConsumer;
import x590.util.io.UncheckedOutputStream;

/**
 * Класс, представляющий реализацию {@link OutputStream}.
 * Содержит много удобных методов для записи текста и объектов.
 */
public abstract class ExtendedOutputStream<Self extends ExtendedOutputStream<Self>> extends UncheckedOutputStream {
	
	private final OutputStream out;
	
	private int indentWidth;
	private String indent = "";
	
	private static final int INDENT_CACHE_SIZE = 64;
	private static final String[] INDENT_CACHE = new String[INDENT_CACHE_SIZE];
	
	static {
		INDENT_CACHE[0] = "";
	}
	
	
	public ExtendedOutputStream(OutputStream out) {
		this.out = Objects.requireNonNull(out);
	}
	
	
	private static String getIndent(int width) {
		if(width < INDENT_CACHE_SIZE) {
			String indent = INDENT_CACHE[width];
			
			if(indent == null)
				indent = INDENT_CACHE[width] = JDecompiler.getConfig().getIndent().repeat(width);
			
			return indent;
		}
		
		return JDecompiler.getConfig().getIndent().repeat(width);
	}
	
	
	@SuppressWarnings("unchecked")
	protected Self self() {
		return (Self)this;
	}
	
	public Self increaseIndent() {
		indent = getIndent(indentWidth += 1);
		return self();
	}
	
	
	public Self resetIndent() {
		indentWidth = 0;
		indent = "";
		return self();
	}
	
	public Self increaseIndent(int count) {
		indent = getIndent(indentWidth += count);
		return self();
	}
	
	public Self reduceIndent() {
		indent = getIndent(indentWidth -= 1);
		return self();
	}
	
	public Self reduceIndent(int count) {
		indent = getIndent(indentWidth -= count);
		return self();
	}
	
	public String getIndent() {
		return indent;
	}
	
	
	public void writeIndent() {
		write(indent);
	}
	
	public Self printIndent() {
		return print(indent);
	}
	
	
	@Override
	public void write(int b) {
		try {
			out.write(b);
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	
	public void write(char ch) {
		try {
			if((ch & 0xFF00) != 0)
				out.write(ch >>> 8);
			
			out.write(ch);
			
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	public void write(String str) {
		try {
			out.write(str.getBytes());
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	
	public Self println() {
		return print('\n');
	}
	
	public Self printsp() {
		return print(' ');
	}

	
	public Self print(char ch) {
		write(ch);
		return self();
	}
	
	public Self println(char ch) {
		return print(ch).print('\n');
	}
	
	public Self printsp(char ch) {
		return print(ch).print(' ');
	}
	
	
	public Self print(String str) {
		write(str);
		return self();
	}
	
	public Self println(String str) {
		return print(str).print('\n');
	}
	
	public Self printsp(String str) {
		return print(str).print(' ');
	}
	
	
	public Self printAll(Collection<? extends String> values) {
		return printAll(values, ' ');
	}
	
	public Self printAll(Collection<? extends String> values, char delimeter) {
		LoopUtil.forEachExcludingLast(values, value -> write(value), value -> write(delimeter));
		return self();
	}
	
	public Self printAll(Collection<? extends String> values, String delimeter) {
		LoopUtil.forEachExcludingLast(values, value -> write(value), value -> write(delimeter));
		return self();
	}
	
	
	public Self printEach(Collection<? extends String> values) {
		return printEach(values, ' ');
	}
	
	public Self printEach(Collection<? extends String> values, char delimeter) {
		values.forEach(value -> print(value).write(delimeter));
		return self();
	}
	
	public Self printEach(Collection<? extends String> values, String delimeter) {
		values.forEach(value -> print(value).write(delimeter));
		return self();
	}
	
	
	/**
	 * Тип Writable<T> должен соответствовать типу потока, за это отвечает пользователь метода
	 */
	protected abstract <T> void write(Writable<T> writable, T param);
	
	
	public <T> Self printObject(Writable<T> writable, T param) {
		write(writable, param);
		return self();
	}
	
	public <T> Self printObjectIfNotNull(@Nullable Writable<T> writable, T param) {
		if(writable != null)
			write(writable, param);
		
		return self();
	}
	
	public <T, W extends Writable<T>> Self printObjectUsingFunction(W writable, Consumer<? super W> writer) {
		writer.accept(writable);
		return self();
	}
	
	public <T, W extends Writable<T>> Self printObjectUsingFunction(W writable, BiConsumer<? super W, ? super Self> writer) {
		writer.accept(writable, self());
		return self();
	}
	
	public <T, W extends Writable<T>> Self printObjectUsingFunction(W writable, T param, TriConsumer<? super W, ? super Self, ? super T> writer) {
		writer.accept(writable, self(), param);
		return self();
	}
	
	
	public <T> Self printlnObject(Writable<T> writable, T param) {
		return printObject(writable, param).print('\n');
	}
	
	public <T> Self printspObject(Writable<T> writable, T param) {
		return printObject(writable, param).print(' ');
	}
	
	
	public <T, W extends Writable<T>> Self printAllObjects(Collection<? extends W> writables, T param) {
		return printAllObjects(writables, param, ' ');
	}
	
	public <T, W extends Writable<T>> Self printAllObjects(Collection<? extends W> writables, T param, char delimeter) {
		return printAllObjects(writables, param, writable -> write(delimeter));
	}
	
	public <T, W extends Writable<T>> Self printAllObjects(Collection<? extends W> writables, T param, String delimeter) {
		return printAllObjects(writables, param, writable -> write(delimeter));
	}
	
	public <T, W extends Writable<T>> Self printAllObjects(Collection<? extends W> writables, T param, Consumer<? super W> delimeterWriter) {
		return printAllObjectsUsingFunction(writables, writable -> write(writable, param), delimeterWriter);
	}
	

	@Deprecated
	public <T, W extends Writable<T>> Self printAllObjects(Collection<? extends W> writables, int startIndex, T param) {
		return printAllObjects(writables, startIndex, param, ' ');
	}

	@Deprecated
	public <T, W extends Writable<T>> Self printAllObjects(Collection<? extends W> writables, int startIndex, T param, char delimeter) {
		return printAllObjects(writables, startIndex, param, writable -> write(delimeter));
	}

	@Deprecated
	public <T, W extends Writable<T>> Self printAllObjects(Collection<? extends W> writables, int startIndex, T param, String delimeter) {
		return printAllObjects(writables, startIndex, param, writable -> write(delimeter));
	}

	@Deprecated
	public <T, W extends Writable<T>> Self printAllObjects(Collection<? extends W> writables, int startIndex, T param, Consumer<? super W> delimeterWriter) {
		LoopUtil.forEachExcludingLast(writables, writable -> write(writable, param), delimeterWriter, startIndex);
		return self();
	}
	
	
	public <T, W extends Writable<T>> Self printAllObjectsUsingFunction(Collection<? extends W> writables, Consumer<? super W> writer) {
		return printAllObjectsUsingFunction(writables, writer, ' ');
	}
	
	public <T, W extends Writable<T>> Self printAllObjectsUsingFunction(Collection<? extends W> writables, Consumer<? super W> writer, char delimeter) {
		return printAllObjectsUsingFunction(writables, writer, writable -> write(delimeter));
	}
	
	public <T, W extends Writable<T>> Self printAllObjectsUsingFunction(Collection<? extends W> writables, Consumer<? super W> writer, String delimeter) {
		return printAllObjectsUsingFunction(writables, writer, writable -> write(delimeter));
	}
	
	public <T, W extends Writable<T>> Self printAllObjectsUsingFunction(Collection<? extends W> writables, Consumer<? super W> writer, Consumer<? super W> delimeterWriter) {
		LoopUtil.forEachExcludingLast(writables, writer, delimeterWriter);
		return self();
	}
	

	@Deprecated
	public <T, W extends Writable<T>> Self printAllObjectsUsingFunction(Collection<? extends W> writables, int startIndex, T param) {
		return printAllObjectsUsingFunction(writables, startIndex, param, ' ');
	}

	@Deprecated
	public <T, W extends Writable<T>> Self printAllObjectsUsingFunction(Collection<? extends W> writables, int startIndex, T param, char delimeter) {
		return printAllObjectsUsingFunction(writables, startIndex, param, writable -> write(delimeter));
	}

	@Deprecated
	public <T, W extends Writable<T>> Self printAllObjectsUsingFunction(Collection<? extends W> writables, int startIndex, T param, String delimeter) {
		return printAllObjectsUsingFunction(writables, startIndex, param, writable -> write(delimeter));
	}

	@Deprecated
	public <T, W extends Writable<T>> Self printAllObjectsUsingFunction(Collection<? extends W> writables, int startIndex, T param, Consumer<? super W> delimeterWriter) {
		LoopUtil.forEachExcludingLast(writables, writable -> write(writable, param), delimeterWriter, startIndex);
		return self();
	}
	
	
	public <T> Self printEachObject(Collection<? extends Writable<T>> writables, T param) {
		return printEachObjectUsingFunction(writables, writable -> write(writable, param));
	}
	
	public <T> Self printEachObjectUsingFunction(Collection<? extends Writable<T>> writables, Consumer<Writable<T>> writer) {
		writables.forEach(writer);
		return self();
	}
	
	
	@Override
	public void flush() {
		try {
			out.flush();
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
	
	@Override
	public void close() {
		try {
			out.close();
		} catch(IOException ex) {
			throw newUncheckedException(ex);
		}
	}
}
