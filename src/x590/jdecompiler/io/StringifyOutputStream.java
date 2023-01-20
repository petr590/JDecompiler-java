package x590.jdecompiler.io;

import java.io.IOException;
import java.io.OutputStream;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.StringWritable;
import x590.jdecompiler.Stringified;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.exception.Operation.Associativity;
import x590.jdecompiler.main.JDecompiler;
import x590.util.annotation.Nullable;

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
	
	public void write(StringWritable writable, ClassInfo classinfo) {
		writable.writeTo(this, classinfo);
	}
	
	public void write(Stringified stringified, ClassInfo classinfo) {
		write(stringified.toString(classinfo));
	}
	
	public <T extends Object /* Genius :) */ & StringWritable & Stringified> void write(T writable, ClassInfo classinfo) {
		write((StringWritable)writable, classinfo);
	}
	
	public void write(Operation operation, StringifyContext context) {
		operation.writeTo(this, context);
	}
	
	
	public void writeIfNotNull(@Nullable StringWritable writable, ClassInfo classinfo) {
		if(writable != null)
			write(writable, classinfo);
	}
	
	public void writeIfNotNull(@Nullable Stringified stringified, ClassInfo classinfo) {
		if(stringified != null)
			write(stringified, classinfo);
	}
	
	public <T extends Object & StringWritable & Stringified> void writeIfNotNull(@Nullable T writable, ClassInfo classinfo) {
		if(writable != null)
			write(writable, classinfo);
	}
	
	public void writeIfNotNull(@Nullable Operation operation, StringifyContext context) {
		if(operation != null)
			write(operation, context);
	}
	
	
	public StringifyOutputStream print(char ch) {
		write(ch);
		return this;
	}
	
	public StringifyOutputStream print(String str) {
		write(str);
		return this;
	}
	
	public StringifyOutputStream print(StringWritable writable, ClassInfo classinfo) {
		write(writable, classinfo);
		return this;
	}
	
	public StringifyOutputStream print(Stringified stringified, ClassInfo classinfo) {
		write(stringified, classinfo);
		return this;
	}
	
	public <T extends Object & StringWritable & Stringified> StringifyOutputStream print(@Nullable T writable, ClassInfo classinfo) {
		write(writable, classinfo);
		return this;
	}
	
	public StringifyOutputStream print(Operation operation, StringifyContext context) {
		write(operation, context);
		return this;
	}
	
	
	public StringifyOutputStream printIfNotNull(@Nullable StringWritable writable, ClassInfo classinfo) {
		writeIfNotNull(writable, classinfo);
		return this;
	}
	
	public StringifyOutputStream printIfNotNull(@Nullable Stringified stringified, ClassInfo classinfo) {
		writeIfNotNull(stringified, classinfo);
		return this;
	}
	
	public <T extends Object & StringWritable & Stringified> StringifyOutputStream printIfNotNull(@Nullable T writable, ClassInfo classinfo) {
		writeIfNotNull(writable, classinfo);
		return this;
	}
	
	public StringifyOutputStream printIfNotNull(@Nullable Operation operation, StringifyContext context) {
		writeIfNotNull(operation, context);
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
	
	public void writeln(StringWritable writable, ClassInfo classinfo) {
		write(writable, classinfo);
		write('\n');
	}
	
	public void writeln(Operation operation, StringifyContext context) {
		write(operation, context);
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
	
	public StringifyOutputStream println(StringWritable writable, ClassInfo classinfo) {
		return print(writable, classinfo).print('\n');
	}
	
	public StringifyOutputStream println(Operation operation, StringifyContext context) {
		return print(operation, context).print('\n');
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
	
	public void writesp(StringWritable writable, ClassInfo classinfo) {
		write(writable, classinfo);
		write(' ');
	}
	
	public void writesp(Operation operation, StringifyContext context) {
		write(operation, context);
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
	
	public StringifyOutputStream printsp(StringWritable writable, ClassInfo classinfo) {
		return print(writable, classinfo).print(' ');
	}
	
	public StringifyOutputStream printsp(Operation operation, StringifyContext context) {
		return print(operation, context).print(' ');
	}
	
	
	public void writePrioritied(Operation thisOperation, Operation otherOperation, StringifyContext context, Associativity associativity) {
		thisOperation.writePrioritied(this, otherOperation, context, associativity);
	}
	
	public void writePrioritied(Operation thisOperation, Operation otherOperation, StringifyContext context, int thisPriority, Associativity associativity) {
		thisOperation.writePrioritied(this, otherOperation, context, thisPriority, associativity);
	}
	
	
	public StringifyOutputStream printPrioritied(Operation thisOperation, Operation otherOperation, StringifyContext context, Associativity associativity) {
		writePrioritied(thisOperation, otherOperation, context, associativity);
		return this;
	}
	
	public StringifyOutputStream printPrioritied(Operation thisOperation, Operation otherOperation, StringifyContext context, int thisPriority, Associativity associativity) {
		writePrioritied(thisOperation, otherOperation, context, thisPriority, associativity);
		return this;
	}
	
	
	@Override
	public void flush() {
		try {
			out.flush();
		} catch (IOException ex) {
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
