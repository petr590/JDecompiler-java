package x590.jdecompiler.io;

import java.io.IOException;
import java.io.OutputStream;

import x590.jdecompiler.StringWritable;
import x590.jdecompiler.StringWritableWithTwoArgs;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.Operation.Associativity;
import x590.util.annotation.Nullable;

/**
 * Класс, представляющий реализацию {@link OutputStream}.
 * Содержит много удобных методов для записи текста.
 */
public class StringifyOutputStream extends UncheckedOutputStream {
	
	private final OutputStream out;
	
	private int indentWidth;
	private String indent = "";
	
	private static final int INDENT_CACHE_SIZE = 64;
	private static final String[] INDENT_CACHE = new String[INDENT_CACHE_SIZE];
	
	static {
		INDENT_CACHE[0] = "";
	}
	
	
	public StringifyOutputStream(OutputStream out) {
		this.out = out;
	}
	
	
	private static String getIndent(int width) {
		if(width < INDENT_CACHE_SIZE) {
			String indent = INDENT_CACHE[width];
			
			if(indent == null)
				indent = INDENT_CACHE[width] = JDecompiler.getInstance().getIndent().repeat(width);
			
			return indent;
		}
		
		return JDecompiler.getInstance().getIndent().repeat(width);
	}
	
	
	public StringifyOutputStream increaseIndent() {
		indent = getIndent(indentWidth += 1);
		return this;
	}
	
	
	public StringifyOutputStream resetIndent() {
		indentWidth = 0;
		indent = "";
		return this;
	}
	
	public StringifyOutputStream increaseIndent(int count) {
		indent = getIndent(indentWidth += count);
		return this;
	}
	
	public StringifyOutputStream reduceIndent() {
		indent = getIndent(indentWidth -= 1);
		return this;
	}
	
	public StringifyOutputStream reduceIndent(int count) {
		indent = getIndent(indentWidth -= count);
		return this;
	}
	
	public String getIndent() {
		return indent;
	}
	
	
	public void writeIndent() {
		write(indent);
	}
	
	public StringifyOutputStream printIndent() {
		return print(indent);
	}
	
	
	@Override
	public void write(int b) {
		try {
			out.write(b);
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	
	public void write(char ch) {
		try {
			if((ch & 0xFF00) != 0)
				out.write(ch >> 8);
			
			out.write(ch);
			
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	public void write(String str) {
		try {
			out.write(str.getBytes());
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	public <T> void write(StringWritable<T> writable, T param) {
		writable.writeTo(this, param);
	}
	
	public <T, U> void write(StringWritableWithTwoArgs<T, U> writable, T param1, U param2) {
		writable.writeTo(this, param1, param2);
	}
	
	
	public <T> void writeIfNotNull(@Nullable StringWritable<T> writable, T param) {
		if(writable != null)
			write(writable, param);
	}
	
	public <T, U> void writeIfNotNull(@Nullable StringWritableWithTwoArgs<T, U> writable, T param1, U param2) {
		if(writable != null)
			writable.writeTo(this, param1, param2);
	}
	
	
	public StringifyOutputStream print(char ch) {
		write(ch);
		return this;
	}
	
	public StringifyOutputStream print(String str) {
		write(str);
		return this;
	}
	
	public <T> StringifyOutputStream print(StringWritable<T> writable, T param) {
		write(writable, param);
		return this;
	}
	
	public <T, U> StringifyOutputStream print(StringWritableWithTwoArgs<T, U> writable, T param1, U param2) {
		write(writable, param1, param2);
		return this;
	}
	
	
	public <T> StringifyOutputStream printIfNotNull(@Nullable StringWritable<T> writable, T param) {
		writeIfNotNull(writable, param);
		return this;
	}
	
	public <T, U> StringifyOutputStream printIfNotNull(@Nullable StringWritableWithTwoArgs<T, U> writable, T param1, U param2) {
		writeIfNotNull(writable, param1, param2);
		return this;
	}
	
	
	public void writeln() {
		write('\n');
	}
	
	public void writeln(char ch) {
		write(ch);
		write('\n');
	}
	
	public void writeln(String str) {
		write(str);
		write('\n');
	}
	
	public <T> void writeln(StringWritable<T> writable, T param) {
		write(writable, param);
		write('\n');
	}
	
	public <T, U> void writeln(StringWritableWithTwoArgs<T, U> writable, T param1, U param2) {
		write(writable, param1, param2);
		write('\n');
	}
	
	
	public StringifyOutputStream println() {
		return print('\n');
	}
	
	public StringifyOutputStream println(char ch) {
		return print(ch).print('\n');
	}
	
	public StringifyOutputStream println(String str) {
		return print(str).print('\n');
	}
	
	public <T> StringifyOutputStream println(StringWritable<T> writable, T param) {
		return print(writable, param).print('\n');
	}
	
	public <T, U> StringifyOutputStream println(StringWritableWithTwoArgs<T, U> writable, T param1, U param2) {
		return print(writable, param1, param2).print('\n');
	}
	
	
	public void writesp() {
		write(' ');
	}
	
	public void writesp(char ch) {
		write(ch);
		write(' ');
	}
	
	public void writesp(String str) {
		write(str);
		write(' ');
	}
	
	public <T> void writesp(StringWritable<T> writable, T param) {
		write(writable, param);
		write(' ');
	}
	
	public <T, U> void writesp(StringWritableWithTwoArgs<T, U> writable, T param1, U param2) {
		write(writable, param1, param2);
		write(' ');
	}
	
	
	public StringifyOutputStream printsp() {
		return print(' ');
	}
	
	public StringifyOutputStream printsp(char ch) {
		return print(ch).print(' ');
	}
	
	public StringifyOutputStream printsp(String str) {
		return print(str).print(' ');
	}
	
	public <T> StringifyOutputStream printsp(StringWritable<T> writable, T param) {
		return print(writable, param).print(' ');
	}
	
	public <T, U> StringifyOutputStream printsp(StringWritableWithTwoArgs<T, U> writable, T param1, U param2) {
		return print(writable, param1, param2).print(' ');
	}
	
	
	public void writePrioritied(Operation thisOperation, Operation otherOperation, StringifyContext param, Associativity associativity) {
		thisOperation.writePrioritied(this, otherOperation, param, associativity);
	}
	
	public void writePrioritied(Operation thisOperation, Operation otherOperation, StringifyContext param, int thisPriority, Associativity associativity) {
		thisOperation.writePrioritied(this, otherOperation, param, thisPriority, associativity);
	}
	
	
	public StringifyOutputStream printPrioritied(Operation thisOperation, Operation otherOperation, StringifyContext param, Associativity associativity) {
		writePrioritied(thisOperation, otherOperation, param, associativity);
		return this;
	}
	
	public StringifyOutputStream printPrioritied(Operation thisOperation, Operation otherOperation, StringifyContext param, int thisPriority, Associativity associativity) {
		writePrioritied(thisOperation, otherOperation, param, thisPriority, associativity);
		return this;
	}
	
	
	@Override
	public void flush() {
		try {
			out.flush();
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
	
	@Override
	public void close() {
		try {
			out.close();
		} catch(IOException ex) {
			throw getUncheckedException(ex);
		}
	}
}
