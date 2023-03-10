package x590.jdecompiler.example;

public class NonStaticFieldsIncrement {
	
	public byte b;
	public short s;
	public char c;
	public int i;
	public long l;
	public float f;
	public double d;
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(NonStaticFieldsIncrement.class);
	}
	
	public void inc() {
		System.out.println(b++);
		System.out.println(s++);
		System.out.println(c++);
		System.out.println(i++);
		System.out.println(l++);
		System.out.println(f++);
		System.out.println(d++);
	}
	
	public void dec() {
		System.out.println(b--);
		System.out.println(s--);
		System.out.println(c--);
		System.out.println(i--);
		System.out.println(l--);
		System.out.println(f--);
		System.out.println(d--);
	}
	
	public void preInc() {
		System.out.println(++b);
		System.out.println(++s);
		System.out.println(++c);
		System.out.println(++i);
		System.out.println(++l);
		System.out.println(++f);
		System.out.println(++d);
	}
	
	public void preDec() {
		System.out.println(--b);
		System.out.println(--s);
		System.out.println(--c);
		System.out.println(--i);
		System.out.println(--l);
		System.out.println(--f);
		System.out.println(--d);
	}
	
	public void add() {
		System.out.println(b += b);
		System.out.println(s += s);
		System.out.println(c += c);
		System.out.println(i += i);
		System.out.println(l += l);
		System.out.println(f += f);
		System.out.println(d += d);
	}
	
	public void sub() {
		System.out.println(b -= b);
		System.out.println(s -= s);
		System.out.println(c -= c);
		System.out.println(i -= i);
		System.out.println(l -= l);
		System.out.println(f -= f);
		System.out.println(d -= d);
	}
	
	public void mul() {
		System.out.println(b *= b);
		System.out.println(s *= s);
		System.out.println(c *= c);
		System.out.println(i *= i);
		System.out.println(l *= l);
		System.out.println(f *= f);
		System.out.println(d *= d);
	}
	
	public void div() {
		System.out.println(b /= b);
		System.out.println(s /= s);
		System.out.println(c /= c);
		System.out.println(i /= i);
		System.out.println(l /= l);
		System.out.println(f /= f);
		System.out.println(d /= d);
	}
	
	public void rem() {
		System.out.println(b %= b);
		System.out.println(s %= s);
		System.out.println(c %= c);
		System.out.println(i %= i);
		System.out.println(l %= l);
		System.out.println(f %= f);
		System.out.println(d %= d);
	}
	
	public void and() {
		System.out.println(b &= b);
		System.out.println(s &= s);
		System.out.println(c &= c);
		System.out.println(i &= i);
		System.out.println(l &= l);
	}
	
	public void or() {
		System.out.println(b |= b);
		System.out.println(s |= s);
		System.out.println(c |= c);
		System.out.println(i |= i);
		System.out.println(l |= l);
	}
	
	public void xor() {
		System.out.println(b ^= b);
		System.out.println(s ^= s);
		System.out.println(c ^= c);
		System.out.println(i ^= i);
		System.out.println(l ^= l);
	}
	
	public void not() {
		System.out.println(b = (byte)~b);
		System.out.println(s = (short)~s);
		System.out.println(c = (char)~c);
		System.out.println(i = ~i);
		System.out.println(l = ~l);
	}
	
	public void neg() {
		System.out.println(b = (byte)-b);
		System.out.println(s = (short)-s);
		System.out.println(c = (char)-c);
		System.out.println(i = -i);
		System.out.println(l = -l);
	}
	
	public void shl() {
		System.out.println(b <<= b);
		System.out.println(s <<= s);
		System.out.println(c <<= c);
		System.out.println(i <<= i);
		System.out.println(l <<= l);
	}
	
	public void shr() {
		System.out.println(b >>= b);
		System.out.println(s >>= s);
		System.out.println(c >>= c);
		System.out.println(i >>= i);
		System.out.println(l >>= l);
	}
	
	public void ushr() {
		System.out.println(b >>>= b);
		System.out.println(s >>>= s);
		System.out.println(c >>>= c);
		System.out.println(i >>>= i);
		System.out.println(l >>>= l);
	}
	
	public void assign() {
		System.out.println(b = -1);
		System.out.println(s = -1);
		System.out.println(c = '\uFFFF');
		System.out.println(i = -1);
		System.out.println(l = -1);
		System.out.println(f = -1);
		System.out.println(d = -1);
	}
}