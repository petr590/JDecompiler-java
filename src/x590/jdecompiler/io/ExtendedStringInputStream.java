package x590.jdecompiler.io;

import java.io.IOException;
import java.io.UncheckedIOException;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntStack;
import x590.util.io.UncheckedInputStream;

/**
 * Выбрасывает {@link UncheckedIOException} вместо {@link IOException}.
 * Также содержит некоторые методы для удобства,
 * такие как {@link #readAll()}
 */
public class ExtendedStringInputStream extends UncheckedInputStream {
	
	private String source;
	private int pos = 0;
	private IntStack marks = new IntArrayList();
	private final int length;
	
	public static final int EOF_CHAR = -1;
	
	
	public ExtendedStringInputStream(String str) {
		this.source = str;
		this.length = str.length();
	}
	
	@Override
	public int available() {
		return length - pos;
	}
	
	public boolean isAvailable() {
		return pos < length;
	}
	
	
	public int getPos() {
		return pos;
	}
	
	public int getMarkPos() {
		return marks.isEmpty() ? -1 : marks.topInt();
	}
	
	public int getMarkOrCurrentPos() {
		return marks.isEmpty() ? pos : marks.topInt();
	}
	
	public int distanceToMark() {
		return pos - getMarkPos();
	}
	
	
	public void incPos() {
		pos++;
	}
	
	public void decPos() {
		if(pos == 0)
			throw newUncheckedException("Position is 0");
		
		pos--;
	}
	
	public ExtendedStringInputStream next() {
		incPos();
		return this;
	}
	
	public ExtendedStringInputStream prev() {
		decPos();
		return this;
	}
	
	
	@Override
	public int read() {
		return pos >= length ? EOF_CHAR : source.charAt(pos++);
	}
	
	public String readAll() {
		String str = source.substring(pos);
		pos = length;
		return str;
	}
	
	public String readString(int length) {
		int newPos = pos + length;
		
		if(newPos > this.length)
			throw new IndexOutOfBoundsException(newPos);
		
		return source.substring(pos, pos = newPos);
	}
	
	public String readString(int startPos, int endPos) {
		if(startPos > length)
			throw new IndexOutOfBoundsException(startPos);
		
		if(startPos > endPos)
			throw new IndexOutOfBoundsException("startPos > endPos");
		
		pos = endPos;
		return source.substring(startPos, endPos);
	}
	
	
	public String readUntil(char ch) {
		return readUntil(ch, new StringBuilder()).toString();
	}
	
	public String readUntil(char ch1, char ch2) {
		return readUntil(ch1, ch2, new StringBuilder()).toString();
	}
	
	
	public StringBuilder readUntil(char ch, StringBuilder str) {
		
		while(true) {
			int c = read();
			if(c == ch || c == EOF_CHAR) break;
			str.append((char)c);
		}
		
		decPos();
		
		return str;
	}
	
	public StringBuilder readUntil(char ch1, char ch2, StringBuilder str) {
		
		while(true) {
			int c = read();
			if(c == ch1 || c == ch2 || c == EOF_CHAR) break;
			str.append((char)c);
		}
		
		decPos();
		
		return str;
	}
	
	
	/** Читает символ с индекса mark.
	 * Если mark = -1, то читает с текущей позиции.
	 * Не изменяет mark и pos. */
	public int getFromMark() {
		return source.charAt(getMarkOrCurrentPos());
	}
	
	/** Читает строку с индекса mark.
	 * Если mark = -1, то читает с текущей позиции.
	 * Не изменяет mark и pos. */
	public String getAllFromMark() {
		return source.substring(getMarkOrCurrentPos());
	}
	
	public String getStringFromMark(int length) {
		int newPos = (getMarkOrCurrentPos()) + length;
		
		if(newPos > this.length)
			throw new IndexOutOfBoundsException(newPos);
		
		return source.substring(pos, newPos);
	}
	
	
	public int get() {
		return pos >= length ? EOF_CHAR : source.charAt(pos);
	}
	
	
	/** Делгирует методу {@link #mark()}. Параметр readAheadLimit игнорируется */
	@Override
	public void mark(int readAheadLimit) {
		this.mark();
	}
	
	public void mark() {
		marks.push(Math.min(pos, length));
	}
	
	public void unmark() {
		try {
			marks.popInt();
		} catch(IndexOutOfBoundsException ex) {
			throw newUncheckedException("Not marked");
		}
	}
	
	public boolean marked() {
		return !marks.isEmpty();
	}
	
	
	@Override
	public void reset() {
		if(marks.isEmpty())
			throw newUncheckedException("Not marked");
		
		pos = marks.popInt();
	}
	
	@Override
	public void close() {
		source = null;
		marks = null;
	}
}
