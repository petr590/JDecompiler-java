package x590.jdecompiler.example.increment;

import x590.jdecompiler.example.ExampleTesting;

public class ArrayIncrement2 {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ArrayIncrement2.class);
	}
	
	public static void foo() {
		
		byte b[] = {1};
		short s[] = {1};
		char c[] = {1};
		int i[] = {1};
		long l[] = {1};
		float f[] = {1};
		double d[] = {1};
		
		b[0]++;
		s[0]++;
		c[0]++;
		i[0]++;
		l[0]++;
		f[0]++;
		d[0]++;
	}
	
	public static void inc(byte b[], short s[], char c[], int i[], long l[], float f[], double d[]) {
		b[0]++;
		s[0]++;
		c[0]++;
		i[0]++;
		l[0]++;
		f[0]++;
		d[0]++;
	}
	
	public static void dec(byte b[], short s[], char c[], int i[], long l[], float f[], double d[]) {
		b[0]--;
		s[0]--;
		c[0]--;
		i[0]--;
		l[0]--;
		f[0]--;
		d[0]--;
	}
	
	public static void preInc(byte b[], short s[], char c[], int i[], long l[], float f[], double d[]) {
		++b[0];
		++s[0];
		++c[0];
		++i[0];
		++l[0];
		++f[0];
		++d[0];
	}
	
	public static void preDec(byte b[], short s[], char c[], int i[], long l[], float f[], double d[]) {
		--b[0];
		--s[0];
		--c[0];
		--i[0];
		--l[0];
		--f[0];
		--d[0];
	}
	
	public static void add(byte b[], short s[], char c[], int i[], long l[], float f[], double d[]) {
		b[0] += b[0];
		s[0] += s[0];
		c[0] += c[0];
		i[0] += i[0];
		l[0] += l[0];
		f[0] += f[0];
		d[0] += d[0];
	}
	
	public static void sub(byte b[], short s[], char c[], int i[], long l[], float f[], double d[]) {
		b[0] -= b[0];
		s[0] -= s[0];
		c[0] -= c[0];
		i[0] -= i[0];
		l[0] -= l[0];
		f[0] -= f[0];
		d[0] -= d[0];
	}
	
	public static void mul(byte b[], short s[], char c[], int i[], long l[], float f[], double d[]) {
		b[0] *= b[0];
		s[0] *= s[0];
		c[0] *= c[0];
		i[0] *= i[0];
		l[0] *= l[0];
		f[0] *= f[0];
		d[0] *= d[0];
	}
	
	public static void div(byte b[], short s[], char c[], int i[], long l[], float f[], double d[]) {
		b[0] /= b[0];
		s[0] /= s[0];
		c[0] /= c[0];
		i[0] /= i[0];
		l[0] /= l[0];
		f[0] /= f[0];
		d[0] /= d[0];
	}
	
	public static void rem(byte b[], short s[], char c[], int i[], long l[], float f[], double d[]) {
		b[0] %= b[0];
		s[0] %= s[0];
		c[0] %= c[0];
		i[0] %= i[0];
		l[0] %= l[0];
		f[0] %= f[0];
		d[0] %= d[0];
	}
	
	public static void and(boolean z[], byte b[], short s[], char c[], int i[], long l[]) {
		z[0] &= z[0];
		b[0] &= b[0];
		s[0] &= s[0];
		c[0] &= c[0];
		i[0] &= i[0];
		l[0] &= l[0];
	}
	
	public static void or(boolean z[], byte b[], short s[], char c[], int i[], long l[]) {
		z[0] |= z[0];
		b[0] |= b[0];
		s[0] |= s[0];
		c[0] |= c[0];
		i[0] |= i[0];
		l[0] |= l[0];
	}
	
	public static void xor(boolean z[], byte b[], short s[], char c[], int i[], long l[]) {
		z[0] ^= z[0];
		b[0] ^= b[0];
		s[0] ^= s[0];
		c[0] ^= c[0];
		i[0] ^= i[0];
		l[0] ^= l[0];
	}
	
	public static void not(byte b[], short s[], char c[], int i[], long l[]) {
		b[0] = (byte)~b[0];
		s[0] = (short)~s[0];
		c[0] = (char)~c[0];
		i[0] = ~i[0];
		l[0] = ~l[0];
	}
	
	public static void neg(byte b[], short s[], char c[], int i[], long l[]) {
		b[0] = (byte)-b[0];
		s[0] = (short)-s[0];
		c[0] = (char)-c[0];
		i[0] = -i[0];
		l[0] = -l[0];
	}
	
	public static void shl(byte b[], short s[], char c[], int i[], long l[]) {
		b[0] <<= b[0];
		s[0] <<= s[0];
		c[0] <<= c[0];
		i[0] <<= i[0];
		l[0] <<= l[0];
	}
	
	public static void shr(byte b[], short s[], char c[], int i[], long l[]) {
		b[0] >>= b[0];
		s[0] >>= s[0];
		c[0] >>= c[0];
		i[0] >>= i[0];
		l[0] >>= l[0];
	}
	
	public static void ushr(byte b[], short s[], char c[], int i[], long l[]) {
		b[0] >>>= b[0];
		s[0] >>>= s[0];
		c[0] >>>= c[0];
		i[0] >>>= i[0];
		l[0] >>>= l[0];
	}
	
	public static void assign(byte b[], short s[], char c[], int i[], long l[], float f[], double d[]) {
		b[0] = -1;
		s[0] = -1;
		c[0] = '\uFFFF';
		i[0] = -1;
		l[0] = -1;
		f[0] = -1;
		d[0] = -1;
	}
}