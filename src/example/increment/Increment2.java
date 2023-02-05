package example.increment;

import example.ExampleTesting;

@SuppressWarnings("unused")
public class Increment2 {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Increment2.class);
	}
	
	public static void foo() {
		
		byte b = 0;
		short s = 0;
		char c = 0;
		int i = 0;
		long l = 0;
		float f = 0;
		double d = 0;
		
		b++;
		s++;
		c++;
		i++;
		l++;
		f++;
		d++;
	}
	
	public static void inc(byte b, short s, char c, int i, long l, float f, double d) {
		b++;
		s++;
		c++;
		i++;
		l++;
		f++;
		d++;
	}
	
	public static void dec(byte b, short s, char c, int i, long l, float f, double d) {
		b--;
		s--;
		c--;
		i--;
		l--;
		f--;
		d--;
	}
	
	public static void preInc(byte b, short s, char c, int i, long l, float f, double d) {
		++b;
		++s;
		++c;
		++i;
		++l;
		++f;
		++d;
	}
	
	public static void preDec(byte b, short s, char c, int i, long l, float f, double d) {
		--b;
		--s;
		--c;
		--i;
		--l;
		--f;
		--d;
	}
	
	public static void add(byte b, short s, char c, int i, long l, float f, double d) {
		b += b;
		s += s;
		c += c;
		i += i;
		l += l;
		f += f;
		d += d;
	}
	
	public static void sub(byte b, short s, char c, int i, long l, float f, double d) {
		b -= b;
		s -= s;
		c -= c;
		i -= i;
		l -= l;
		f -= f;
		d -= d;
	}
	
	public static void mul(byte b, short s, char c, int i, long l, float f, double d) {
		b *= b;
		s *= s;
		c *= c;
		i *= i;
		l *= l;
		f *= f;
		d *= d;
	}
	
	public static void div(byte b, short s, char c, int i, long l, float f, double d) {
		b /= b;
		s /= s;
		c /= c;
		i /= i;
		l /= l;
		f /= f;
		d /= d;
	}
	
	public static void rem(byte b, short s, char c, int i, long l, float f, double d) {
		b %= b;
		s %= s;
		c %= c;
		i %= i;
		l %= l;
		f %= f;
		d %= d;
	}
	
	public static void and(byte b, short s, char c, int i, long l) {
		b &= b;
		s &= s;
		c &= c;
		i &= i;
		l &= l;
	}
	
	public static void or(byte b, short s, char c, int i, long l) {
		b |= b;
		s |= s;
		c |= c;
		i |= i;
		l |= l;
	}
	
	public static void xor(byte b, short s, char c, int i, long l) {
		b ^= b;
		s ^= s;
		c ^= c;
		i ^= i;
		l ^= l;
	}
	
	public static void not(byte b, short s, char c, int i, long l) {
		b = (byte)~b;
		s = (short)~s;
		c = (char)~c;
		i = ~i;
		l = ~l;
	}
	
	public static void neg(byte b, short s, char c, int i, long l) {
		b = (byte)-b;
		s = (short)-s;
		c = (char)-c;
		i = -i;
		l = -l;
	}
	
	public static void shl(byte b, short s, char c, int i, long l) {
		b <<= b;
		s <<= s;
		c <<= c;
		i <<= i;
		l <<= l;
	}
	
	public static void shr(byte b, short s, char c, int i, long l) {
		b >>= b;
		s >>= s;
		c >>= c;
		i >>= i;
		l >>= l;
	}
	
	public static void ushr(byte b, short s, char c, int i, long l) {
		b >>>= b;
		s >>>= s;
		c >>>= c;
		i >>>= i;
		l >>>= l;
	}
	
	public static void assign(byte b, short s, char c, int i, long l, float f, double d) {
		b = -1;
		s = -1;
		c = '\uFFFF';
		i = -1;
		l = -1;
		f = -1;
		d = -1;
	}
}