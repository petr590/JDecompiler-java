package x590.javaclass.util;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

import x590.javaclass.exception.DecompilationException;
import x590.jdecompiler.JDecompiler;

public class Util {
	
	public static String toLowerCamelCase(String str) {
		int strlength = str.length();
		
		StringBuilder result = new StringBuilder(strlength);
		
		int i = 0;
		for(; i < strlength; i++) {
			char c = str.charAt(i);
			
			if(c >= 'A' && c <= 'Z')
				result.append(Character.toLowerCase(c));
			else
				break;
		}
		
		while(i < strlength)
			result.append(str.charAt(i++));
		
		return result.toString();
	}
	
	
	public static String hex(int num) {
		return Integer.toHexString(num);
	}
	
	public static String hexWithPrefix(int num) {
		return "0x" + Integer.toHexString(num);
	}
	
	
	private static char hexChar(int num) {
		num &= 0xF;
		return (char)((num > 9 ? 'A' - 10 : '0') + num);
	}
	
	
	public static String hex1(int num) {
		return Character.toString(hexChar(num));
	}
	
	public static String hex2(int num) {
		return String.valueOf(new char[] { hexChar(num >>> 4), hexChar(num) });
	}
	
	public static String hex4(int num) {
		return String.valueOf(new char[] { hexChar(num >>> 12), hexChar(num >>> 8), hexChar(num >>> 4), hexChar(num) });
	}
	
	
	public static String hex1WithPrefix(int num) {
		return "0x" + hex1(num);
	}
	
	public static String hex2WithPrefix(int num) {
		return "0x" + hex2(num);
	}
	
	public static String hex4WithPrefix(int num) {
		return "0x" + hex4(num);
	}
	
	
	private static final Charset UTF8_CHARSET = Charset.forName("Utf-8");
	
	private static String encodeUtf8(int c) {
		// 0xxxxxxx
		if(c < 0x80)       return String.valueOf((char)c);
		// 110xxxxx 10xxxxxx
		if(c < 0x800)      return new String(new byte[] { (byte)((c >>  6 & 0x1F) | 0xC0), (byte)((c & 0x3F) | 0x80) }, UTF8_CHARSET);
		// 1110xxxx 10xxxxxx 10xxxxxx
		if(c < 0x10000)    return new String(new byte[] { (byte)((c >> 12 &  0xF) | 0xE0), (byte)((c >>  6 & 0x3F) | 0x80), (byte)((c >>  0 & 0x3F) | 0x80) }, UTF8_CHARSET);
		// 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
		if(c < 0x200000)   return new String(new byte[] { (byte)((c >> 18 &  0x7) | 0xF0), (byte)((c >> 12 & 0x3F) | 0x80), (byte)((c >>  6 & 0x3F) | 0x80),
									(byte)((c >>  0 & 0x3F) | 0x80) }, UTF8_CHARSET);
		// 111110xx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
		if(c < 0x4000000)  return new String(new byte[] { (byte)((c >> 24 &  0x3) | 0xF8), (byte)((c >> 18 & 0x3F) | 0x80), (byte)((c >> 12 & 0x3F) | 0x80),
									(byte)((c >>  6 & 0x3F) | 0x80), (byte)((c >>  0 & 0x3F) | 0x80) }, UTF8_CHARSET);
		// 1111110x 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx 10xxxxxx
		if(c < 0x80000000) return new String(new byte[] { (byte)((c >> 30 &  0x1) | 0xFC), (byte)((c >> 24 & 0x3F) | 0x80), (byte)((c >> 18 & 0x3F) | 0x80),
									(byte)((c >> 12 & 0x3F) | 0x80), (byte)((c >>  6 & 0x3F) | 0x80), (byte)((c >>  0 & 0x3F) | 0x80) }, UTF8_CHARSET);
		
		throw new IllegalArgumentException("Char code U+" + hex(c) + " is too large for encode");
	}
	
	
	// surrogate pairs in UTF-16: 0xD800-0xDFFF
	private static String escapeUtf16(int ch) {
		assert ch <= 0x10FFFF;
		
		if(ch > 0xFFFF) {
			ch -= 0x10000;
			return "\\u" + hex4(((ch >> 10) & 0x3FF) | 0xD800) + "\\u" + hex4((ch & 0x3FF) | 0xDC00);
		}
		
		return "\\u" + hex4(ch);
	}
	
	private static String escapeUtf16Octal(int ch) {
		assert ch <= 0x10FFFF;
		
		if(ch > 0xFFFF) {
			ch -= 0x10000;
			return "\\" + Integer.toOctalString(((ch >> 10) & 0x3FF) | 0xD800) + "\\" + Integer.toOctalString((ch & 0x3FF) | 0xDC00);
		}
		
		return "\\" + Integer.toOctalString(ch);
	}
	
	private static boolean isNotDisplayedChar(int ch) {
		return ch < 0x20 || (ch >= 0x7F && ch < 0xA0) // Control characters
			|| (ch >= 0xFFEF && ch < 0x10000)         // Unknown characters
			|| (ch >= 0xD800 && ch < 0xE000);         // Surrogate pairs in UTF-16
	}
	
	
	private static String charToString(char quote, int ch) {
		assert quote == '"' || quote == '\'' : "Invalid quote";
		
		switch(ch) {
			case '\b': return "\\b";
			case '\t': return "\\t";
			case '\n': return "\\n";
			case '\f': return "\\f";
			case '\r': return "\\r";
			case '\\': return "\\\\";
			default:   return ch == quote ? "\\" + quote :
					isNotDisplayedChar(ch) || (JDecompiler.getInstance().escapeUnicodeChars() && ch >= 0x80) ?
							quote == '\'' && ch < 0x100 ?
									escapeUtf16Octal(ch) :
									escapeUtf16(ch) :
							encodeUtf8(ch);
		}
	}
	
	public static String toLiteral(String str) {
		byte[] bytes = str.getBytes();
		
		StringBuilder result = new StringBuilder(bytes.length).append('"');
		
		for(int i = 0, end = bytes.length; i < end; ++i) {
			int ch = bytes[i] & 0xFF;
			
			if((ch & 0xE0) == 0xC0) {
				i++;
				
				if(i >= end)
					throw new DecompilationException("Unexpected end of the string: " + i + " >= " + end);
				
				if((bytes[i] & 0xC0) != 0x80)
					throw new DecompilationException("Invalid string encoding");
				
				ch = (ch & 0x1F) << 6 | (bytes[i] & 0x3F);
				
			} else if((ch & 0xF0) == 0xE0) {
				
				if(ch == 0xED && i + 5 < end &&
						(bytes[i + 1] & 0xF0) == 0xA0 && (bytes[i + 2] & 0xC0) == 0x80 && (bytes[i + 3] & 0xFF) == 0xED
					 && (bytes[i + 4] & 0xF0) == 0xB0 && (bytes[i + 5] & 0xC0) == 0x80) {
					
					result.append(encodeUtf8(0x10000 | (bytes[++i] & 0xF) << 16 |
							(bytes[++i] & 0x3F) << 10 | (bytes[i += 2] & 0xF) << 6 | (bytes[++i] & 0x3F)));
					
					continue;
				}
				
				if(i + 2 >= end)
					throw new DecompilationException("Unexpected end of the string: " + i + " + " + 2 + " >= " + end);
				
				if((bytes[i + 1] & 0xC0) != 0x80 || (bytes[i + 2] & 0xC0) != 0x80)
					throw new DecompilationException("Invalid string encoding");
				
				ch = (ch & 0xF) << 12 | (bytes[++i] & 0x3F) << 6 | (bytes[++i] & 0x3F);
			}
			
			if(ch > 0x10FFFF)
				throw new DecompilationException("Invalid string: char code U+" + hex(ch) + " is out of range");
			
			result.append(charToString('"', ch));
		}
		
		return result.append('"').toString();
	}
	
	
	
	public static String toLiteral(boolean value) {
		return Boolean.toString(value);
	}
	
	public static String toLiteral(byte value) {
		return Byte.toString(value);
	}
	
	public static String toLiteral(short value) {
		return Short.toString(value);
	}
	
	public static String toLiteral(char value) {
		return "'" + charToString('\'', value) + "'";
	}
	
	public static String toLiteral(int value) {
		return Integer.toString(value);
	}
	
	public static String toLiteral(long value) {
		return Long.toString(value) + JDecompiler.getInstance().getLongSuffix();
	}
	
	public static String toLiteral(float value) {
		return (JDecompiler.getInstance().printTrailingZero() && (int)value == value ? Integer.toString((int)value) : Float.toString(value)) +
				JDecompiler.getInstance().getFloatSuffix();
	}
	
	public static String toLiteral(double value) {
		return  JDecompiler.getInstance().printTrailingZero() && (int)value == value ?
					Integer.toString((int)value) + JDecompiler.getInstance().getDoubleSuffix() :
				JDecompiler.getInstance().printDoubleSuffix() ?
					Double.toString(value) + JDecompiler.getInstance().getDoubleSuffix() :
					Double.toString(value);
	}
	
	
	public static <T> void forEachExcludingLast(T[] array, Consumer<? super T> eachFunc, Runnable eachExcludingLastFunc) {
		forEachExcludingLast(Arrays.stream(array).iterator(), eachFunc, eachExcludingLastFunc);
	}
	
	public static <T> void forEachExcludingLast(T[] array, Consumer<? super T> eachFunc, Consumer<? super T> eachExcludingLastFunc) {
		forEachExcludingLast(Arrays.stream(array).iterator(), eachFunc, eachExcludingLastFunc);
	}
	
	
	public static <T> void forEachExcludingLast(Iterable<T> iterable, Consumer<? super T> eachFunc, Runnable eachExcludingLastFunc) {
		forEachExcludingLast(iterable.iterator(), eachFunc, eachExcludingLastFunc);
	}
	
	public static <T> void forEachExcludingLast(Iterable<T> iterable, Consumer<? super T> eachFunc, Consumer<? super T> eachExcludingLastFunc) {
		forEachExcludingLast(iterable.iterator(), eachFunc, eachExcludingLastFunc);
	}
	
	
	public static <T> void forEachExcludingLast(Iterator<T> iterator, Consumer<? super T> eachFunc, Runnable eachExcludingLastFunc) {
		forEachExcludingLast(iterator, eachFunc, value -> eachExcludingLastFunc.run());
	}
	
	public static <T> void forEachExcludingLast(Iterator<T> iterator, Consumer<? super T> eachFunc, Consumer<? super T> eachExcludingLastFunc) {
		
		if(iterator.hasNext()) {
			while(true) {
				T value = iterator.next();
				eachFunc.accept(value);
				
				if(iterator.hasNext())
					eachExcludingLastFunc.accept(value);
				else
					break;
			}
		}
	}
	
	
	public static <T> void forEachExcludingLast(T[] array, ObjIntConsumer<? super T> eachFunc, Runnable eachExcludingLastFunc) {
		forEachExcludingLast(Arrays.stream(array).iterator(), eachFunc, eachExcludingLastFunc);
	}
	
	public static <T> void forEachExcludingLast(T[] array, ObjIntConsumer<? super T> eachFunc, Consumer<? super T> eachExcludingLastFunc) {
		forEachExcludingLast(Arrays.stream(array).iterator(), eachFunc, eachExcludingLastFunc);
	}
	
	
	public static <T> void forEachExcludingLast(Iterable<T> iterable, ObjIntConsumer<? super T> eachFunc, Runnable eachExcludingLastFunc) {
		forEachExcludingLast(iterable.iterator(), eachFunc, eachExcludingLastFunc);
	}
	
	public static <T> void forEachExcludingLast(Iterable<T> iterable, ObjIntConsumer<? super T> eachFunc, Consumer<? super T> eachExcludingLastFunc) {
		forEachExcludingLast(iterable.iterator(), eachFunc, eachExcludingLastFunc);
	}
	
	
	public static <T> void forEachExcludingLast(Iterator<T> iterator, ObjIntConsumer<? super T> eachFunc, Runnable eachExcludingLastFunc) {
		forEachExcludingLast(iterator, eachFunc, value -> eachExcludingLastFunc.run());
	}
	
	public static <T> void forEachExcludingLast(Iterator<T> iterator, ObjIntConsumer<? super T> eachFunc, Consumer<? super T> eachExcludingLastFunc) {
		
		if(iterator.hasNext()) {
			int i = 0;
			
			while(true) {
				T value = iterator.next();
				eachFunc.accept(value, i);
				
				if(iterator.hasNext())
					eachExcludingLastFunc.accept(value);
				else
					break;
				
				i++;
			}
		}
	}
	
	
	
	public static <T> void forEach(T[] array, Consumer<T> func) {
		for(T t : array) {
			func.accept(t);
		}
	}
}
