package x590.jdecompiler.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Выбрасывает {@link UncheckedIOException} вместо {@link IOException}.
 * Также содержит некоторые методы для удобства,
 * такие как {@link #readAll()}
 */
public class ExtendedStringReader extends InputStream {
	
	private String source;
	private int pos = 0;
	private List<Integer> marks = new ArrayList<>();
	private final int length;
	public static final int EOF_CHAR = -1;
	
	
	public ExtendedStringReader(String str) {
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
		return marks.isEmpty() ? -1 : marks.get(marks.size() - 1);
	}
	
	public int getMarkOrCurrentPos() {
		return marks.isEmpty() ? pos : marks.get(marks.size() - 1);
	}
	
	public int distanceToMark() {
		return pos - getMarkPos();
	}
	
	
	public void incPos() {
		pos++;
	}
	
	public void decPos() {
		if(pos == 0)
			throw new UncheckedIOException(new IOException("Position is 0"));
		
		pos--;
	}
	
	public ExtendedStringReader next() {
		incPos();
		return this;
	}
	
	public ExtendedStringReader prev() {
		decPos();
		return this;
	}
	
	
	@Override
	public int read() {
		return pos >= length ? ExtendedStringReader.EOF_CHAR : source.charAt(pos++);
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
			if(c == ch || c == ExtendedStringReader.EOF_CHAR) break;
			str.append((char)c);
		}
		
		decPos();
		
		return str;
	}
	
	public StringBuilder readUntil(char ch1, char ch2, StringBuilder str) {
		
		while(true) {
			int c = read();
			if(c == ch1 || c == ch2 || c == ExtendedStringReader.EOF_CHAR) break;
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
		return pos >= length ? ExtendedStringReader.EOF_CHAR : source.charAt(pos);
	}
	
	
	/** Делгирует методу {@link #mark()}. Параметр readAheadLimit игнорируется */
	@Override
	public void mark(int readAheadLimit) {
		this.mark();
	}
	
	public void mark() {
		marks.add(Math.min(pos, length));
	}
	
	public void unmark() {
		try {
			marks.remove(marks.size() - 1);
		} catch(IndexOutOfBoundsException ex) {
			throw new UncheckedIOException(new IOException("Not marked"));
		}
	}
	
	public boolean marked() {
		return !marks.isEmpty();
	}
	
	
	@Override
	public void reset() {
		if(marks.isEmpty())
			throw new UncheckedIOException(new IOException("Not marked"));
		
		pos = marks.remove(marks.size() - 1);
	}
	
	@Override
	public void close() {
		source = null;
	}
}