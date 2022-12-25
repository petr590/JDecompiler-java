package example;

public class Increment1 {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Increment1.class);
	}
	
	public static void foo() {
		
		byte b = 0;
		short s = 0;
		char c = 0;
		int i = 0;
		long l = 0;
		float f = 0;
		double d = 0;
		
		System.out.println(b++);
		System.out.println(s++);
		System.out.println(c++);
		System.out.println(i++);
		System.out.println(l++);
		System.out.println(f++);
		System.out.println(d++);
	}
	
	public static void inc(byte b, short s, char c, int i, long l, float f, double d) {
		System.out.println(b++);
		System.out.println(s++);
		System.out.println(c++);
		System.out.println(i++);
		System.out.println(l++);
		System.out.println(f++);
		System.out.println(d++);
	}
	
	public static void dec(byte b, short s, char c, int i, long l, float f, double d) {
		System.out.println(b--);
		System.out.println(s--);
		System.out.println(c--);
		System.out.println(i--);
		System.out.println(l--);
		System.out.println(f--);
		System.out.println(d--);
	}
	
	public static void preInc(byte b, short s, char c, int i, long l, float f, double d) {
		System.out.println(++b);
		System.out.println(++s);
		System.out.println(++c);
		System.out.println(++i);
		System.out.println(++l);
		System.out.println(++f);
		System.out.println(++d);
	}
	
	public static void preDec(byte b, short s, char c, int i, long l, float f, double d) {
		System.out.println(--b);
		System.out.println(--s);
		System.out.println(--c);
		System.out.println(--i);
		System.out.println(--l);
		System.out.println(--f);
		System.out.println(--d);
	}
	
	public static void add(byte b, short s, char c, int i, long l, float f, double d) {
		System.out.println(b += b);
		System.out.println(s += s);
		System.out.println(c += c);
		System.out.println(i += i);
		System.out.println(l += l);
		System.out.println(f += f);
		System.out.println(d += d);
	}
	
	public static void sub(byte b, short s, char c, int i, long l, float f, double d) {
		System.out.println(b -= b);
		System.out.println(s -= s);
		System.out.println(c -= c);
		System.out.println(i -= i);
		System.out.println(l -= l);
		System.out.println(f -= f);
		System.out.println(d -= d);
	}
	
	public static void mul(byte b, short s, char c, int i, long l, float f, double d) {
		System.out.println(b *= b);
		System.out.println(s *= s);
		System.out.println(c *= c);
		System.out.println(i *= i);
		System.out.println(l *= l);
		System.out.println(f *= f);
		System.out.println(d *= d);
	}
	
	public static void div(byte b, short s, char c, int i, long l, float f, double d) {
		System.out.println(b /= b);
		System.out.println(s /= s);
		System.out.println(c /= c);
		System.out.println(i /= i);
		System.out.println(l /= l);
		System.out.println(f /= f);
		System.out.println(d /= d);
	}
	
	public static void rem(byte b, short s, char c, int i, long l, float f, double d) {
		System.out.println(b %= b);
		System.out.println(s %= s);
		System.out.println(c %= c);
		System.out.println(i %= i);
		System.out.println(l %= l);
		System.out.println(f %= f);
		System.out.println(d %= d);
	}
	
	public static void and(byte b, short s, char c, int i, long l) {
		System.out.println(b &= b);
		System.out.println(s &= s);
		System.out.println(c &= c);
		System.out.println(i &= i);
		System.out.println(l &= l);
	}
	
	public static void or(byte b, short s, char c, int i, long l) {
		System.out.println(b |= b);
		System.out.println(s |= s);
		System.out.println(c |= c);
		System.out.println(i |= i);
		System.out.println(l |= l);
	}
	
	public static void xor(byte b, short s, char c, int i, long l) {
		System.out.println(b ^= b);
		System.out.println(s ^= s);
		System.out.println(c ^= c);
		System.out.println(i ^= i);
		System.out.println(l ^= l);
	}
	
	public static void not(byte b, short s, char c, int i, long l) {
		System.out.println(b = (byte)~b);
		System.out.println(s = (short)~s);
		System.out.println(c = (char)~c);
		System.out.println(i = ~i);
		System.out.println(l = ~l);
	}
	
	public static void neg(byte b, short s, char c, int i, long l) {
		System.out.println(b = (byte)-b);
		System.out.println(s = (short)-s);
		System.out.println(c = (char)-c);
		System.out.println(i = -i);
		System.out.println(l = -l);
	}
	
	public static void shl(byte b, short s, char c, int i, long l) {
		System.out.println(b <<= b);
		System.out.println(s <<= s);
		System.out.println(c <<= c);
		System.out.println(i <<= i);
		System.out.println(l <<= l);
	}
	
	public static void shr(byte b, short s, char c, int i, long l) {
		System.out.println(b >>= b);
		System.out.println(s >>= s);
		System.out.println(c >>= c);
		System.out.println(i >>= i);
		System.out.println(l >>= l);
	}
	
	public static void ushr(byte b, short s, char c, int i, long l) {
		System.out.println(b >>>= b);
		System.out.println(s >>>= s);
		System.out.println(c >>>= c);
		System.out.println(i >>>= i);
		System.out.println(l >>>= l);
	}
	
	public static void assign(byte b, short s, char c, int i, long l, float f, double d) {
		System.out.println(b = -1);
		System.out.println(s = -1);
		System.out.println(c = '\uFFFF');
		System.out.println(i = -1);
		System.out.println(l = -1);
		System.out.println(f = -1);
		System.out.println(d = -1);
	}
}