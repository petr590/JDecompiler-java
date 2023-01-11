package example;

public class StaticFields {
	
	public static byte b = 0;
	public static short s = 0;
	public static char c = 0;
	public static int i = 0;
	public static long l = 0;
	public static float f = 0;
	public static double d = 0;
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(StaticFields.class);
	}
	
	public static void inc() {
		System.out.println(b++);
		System.out.println(s++);
		System.out.println(c++);
		System.out.println(i++);
		System.out.println(l++);
		System.out.println(f++);
		System.out.println(d++);
	}
	
	public static void dec() {
		System.out.println(b--);
		System.out.println(s--);
		System.out.println(c--);
		System.out.println(i--);
		System.out.println(l--);
		System.out.println(f--);
		System.out.println(d--);
	}
	
	public static void preInc() {
		System.out.println(++b);
		System.out.println(++s);
		System.out.println(++c);
		System.out.println(++i);
		System.out.println(++l);
		System.out.println(++f);
		System.out.println(++d);
	}
	
	public static void preDec() {
		System.out.println(--b);
		System.out.println(--s);
		System.out.println(--c);
		System.out.println(--i);
		System.out.println(--l);
		System.out.println(--f);
		System.out.println(--d);
	}
	
	public static void add() {
		System.out.println(b += b);
		System.out.println(s += s);
		System.out.println(c += c);
		System.out.println(i += i);
		System.out.println(l += l);
		System.out.println(f += f);
		System.out.println(d += d);
	}
	
	public static void sub() {
		System.out.println(b -= b);
		System.out.println(s -= s);
		System.out.println(c -= c);
		System.out.println(i -= i);
		System.out.println(l -= l);
		System.out.println(f -= f);
		System.out.println(d -= d);
	}
	
	public static void mul() {
		System.out.println(b *= b);
		System.out.println(s *= s);
		System.out.println(c *= c);
		System.out.println(i *= i);
		System.out.println(l *= l);
		System.out.println(f *= f);
		System.out.println(d *= d);
	}
	
	public static void div() {
		System.out.println(b /= b);
		System.out.println(s /= s);
		System.out.println(c /= c);
		System.out.println(i /= i);
		System.out.println(l /= l);
		System.out.println(f /= f);
		System.out.println(d /= d);
	}
	
	public static void rem() {
		System.out.println(b %= b);
		System.out.println(s %= s);
		System.out.println(c %= c);
		System.out.println(i %= i);
		System.out.println(l %= l);
		System.out.println(f %= f);
		System.out.println(d %= d);
	}
	
	public static void and() {
		System.out.println(b &= b);
		System.out.println(s &= s);
		System.out.println(c &= c);
		System.out.println(i &= i);
		System.out.println(l &= l);
	}
	
	public static void or() {
		System.out.println(b |= b);
		System.out.println(s |= s);
		System.out.println(c |= c);
		System.out.println(i |= i);
		System.out.println(l |= l);
	}
	
	public static void xor() {
		System.out.println(b ^= b);
		System.out.println(s ^= s);
		System.out.println(c ^= c);
		System.out.println(i ^= i);
		System.out.println(l ^= l);
	}
	
	public static void not() {
		System.out.println(b = (byte)~b);
		System.out.println(s = (short)~s);
		System.out.println(c = (char)~c);
		System.out.println(i = ~i);
		System.out.println(l = ~l);
	}
	
	public static void neg() {
		System.out.println(b = (byte)-b);
		System.out.println(s = (short)-s);
		System.out.println(c = (char)-c);
		System.out.println(i = -i);
		System.out.println(l = -l);
	}
	
	public static void shl() {
		System.out.println(b <<= b);
		System.out.println(s <<= s);
		System.out.println(c <<= c);
		System.out.println(i <<= i);
		System.out.println(l <<= l);
	}
	
	public static void shr() {
		System.out.println(b >>= b);
		System.out.println(s >>= s);
		System.out.println(c >>= c);
		System.out.println(i >>= i);
		System.out.println(l >>= l);
	}
	
	public static void ushr() {
		System.out.println(b >>>= b);
		System.out.println(s >>>= s);
		System.out.println(c >>>= c);
		System.out.println(i >>>= i);
		System.out.println(l >>>= l);
	}
	
	public static void assign() {
		System.out.println(b = -1);
		System.out.println(s = -1);
		System.out.println(c = '\uFFFF');
		System.out.println(i = -1);
		System.out.println(l = -1);
		System.out.println(f = -1);
		System.out.println(d = -1);
	}
}