package x590.jdecompiler.example;

public class NonStaticFieldsLoopIncrement {

	public static void main(String[] args) {
		ExampleTesting.runDecompiler(NonStaticFieldsLoopIncrement.class);
	}
	
	byte b;
	short s;
	char c;
	int i;
	long l;
	float f;
	double d;
	
	public void inc() {
		while(b < 1) b++;
		while(s < 1) s++;
		while(c < 1) c++;
		while(i < 1) i++;
		while(l < 1) l++;
		while(f < 1) f++;
		while(d < 1) d++;
	}

	public void dec() {
		while(b < 1) b--;
		while(s < 1) s--;
		while(c < 1) c--;
		while(i < 1) i--;
		while(l < 1) l--;
		while(f < 1) f--;
		while(d < 1) d--;
	}

	public void preInc() {
		while(b < 1) ++b;
		while(s < 1) ++s;
		while(c < 1) ++c;
		while(i < 1) ++i;
		while(l < 1) ++l;
		while(f < 1) ++f;
		while(d < 1) ++d;
	}

	public void preDec() {
		while(b < 1) --b;
		while(s < 1) --s;
		while(c < 1) --c;
		while(i < 1) --i;
		while(l < 1) --l;
		while(f < 1) --f;
		while(d < 1) --d;
	}

	public void add() {
		while(b < 1) b += b;
		while(s < 1) s += s;
		while(c < 1) c += c;
		while(i < 1) i += i;
		while(l < 1) l += l;
		while(f < 1) f += f;
		while(d < 1) d += d;
	}

	public void sub() {
		while(b < 1) b -= b;
		while(s < 1) s -= s;
		while(c < 1) c -= c;
		while(i < 1) i -= i;
		while(l < 1) l -= l;
		while(f < 1) f -= f;
		while(d < 1) d -= d;
	}

	public void mul() {
		while(b < 1) b *= b;
		while(s < 1) s *= s;
		while(c < 1) c *= c;
		while(i < 1) i *= i;
		while(l < 1) l *= l;
		while(f < 1) f *= f;
		while(d < 1) d *= d;
	}

	public void div() {
		while(b < 1) b /= b;
		while(s < 1) s /= s;
		while(c < 1) c /= c;
		while(i < 1) i /= i;
		while(l < 1) l /= l;
		while(f < 1) f /= f;
		while(d < 1) d /= d;
	}

	public void rem() {
		while(b < 1) b %= b;
		while(s < 1) s %= s;
		while(c < 1) c %= c;
		while(i < 1) i %= i;
		while(l < 1) l %= l;
		while(f < 1) f %= f;
		while(d < 1) d %= d;
	}

	public void and() {
		while(b < 1) b &= b;
		while(s < 1) s &= s;
		while(c < 1) c &= c;
		while(i < 1) i &= i;
		while(l < 1) l &= l;
	}

	public void or() {
		while(b < 1) b |= b;
		while(s < 1) s |= s;
		while(c < 1) c |= c;
		while(i < 1) i |= i;
		while(l < 1) l |= l;
	}

	public void xor() {
		while(b < 1) b ^= b;
		while(s < 1) s ^= s;
		while(c < 1) c ^= c;
		while(i < 1) i ^= i;
		while(l < 1) l ^= l;
	}

	public void not() {
		while(b < 1) b = (byte)~b;
		while(s < 1) s = (short)~s;
		while(c < 1) c = (char)~c;
		while(i < 1) i = ~i;
		while(l < 1) l = ~l;
	}

	public void neg() {
		while(b < 1) b = (byte)-b;
		while(s < 1) s = (short)-s;
		while(c < 1) c = (char)-c;
		while(i < 1) i = -i;
		while(l < 1) l = -l;
	}

	public void shl() {
		while(b < 1) b <<= b;
		while(s < 1) s <<= s;
		while(c < 1) c <<= c;
		while(i < 1) i <<= i;
		while(l < 1) l <<= (int)l;
	}

	public void shr() {
		while(b < 1) b >>= b;
		while(s < 1) s >>= s;
		while(c < 1) c >>= c;
		while(i < 1) i >>= i;
		while(l < 1) l >>= (int)l;
	}

	public void ushr() {
		while(b < 1) b >>>= b;
		while(s < 1) s >>>= s;
		while(c < 1) c >>>= c;
		while(i < 1) i >>>= i;
		while(l < 1) l >>>= (int)l;
	}

	public void assign() {
		while(b < 1) b = -1;
		while(s < 1) s = -1;
		while(c < 1) c = '\uFFFF';
		while(i < 1) i = -1;
		while(l < 1) l = -1;
		while(f < 1) f = -1;
		while(d < 1) d = -1;
	}
}