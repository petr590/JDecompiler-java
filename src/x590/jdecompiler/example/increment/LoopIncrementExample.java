package x590.jdecompiler.example.increment;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class LoopIncrementExample {

    public static void main(String[] args) {
        ExampleTesting.runDecompiler(LoopIncrementExample.class);
    }

    public static void foo() {
        byte b = 0;
        short s = 0;
        char c = '\0';
        int n = 0;
        long l = 0;
        float f = 0;
        double d = 0;
        while(b < 1) b++;
        while(s < 1) s++;
        while(c < 1) c++;
        while(n < 1) n++;
        while(l < 1) l++;
        while(f < 1) f++;
        while(d < 1) d++;
    }

    public static void inc(byte b, short s, char c, int i, long l, float f, double d) {
        while(b < 1) b++;
        while(s < 1) s++;
        while(c < 1) c++;
        while(i < 1) i++;
        while(l < 1) l++;
        while(f < 1) f++;
        while(d < 1) d++;
    }

    public static void dec(byte b, short s, char c, int i, long l, float f, double d) {
        while(b < 1) b--;
        while(s < 1) s--;
        while(c < 1) c--;
        while(i < 1) i--;
        while(l < 1) l--;
        while(f < 1) f--;
        while(d < 1) d--;
    }

    public static void preInc(byte b, short s, char c, int i, long l, float f, double d) {
        while(b < 1) ++b;
        while(s < 1) ++s;
        while(c < 1) ++c;
        while(i < 1) ++i;
        while(l < 1) ++l;
        while(f < 1) ++f;
        while(d < 1) ++d;
    }

    public static void preDec(byte b, short s, char c, int i, long l, float f, double d) {
        while(b < 1) --b;
        while(s < 1) --s;
        while(c < 1) --c;
        while(i < 1) --i;
        while(l < 1) --l;
        while(f < 1) --f;
        while(d < 1) --d;
    }

    public static void add(byte b, short s, char c, int i, long l, float f, double d) {
        while(b < 1) b += b;
        while(s < 1) s += s;
        while(c < 1) c += c;
        while(i < 1) i += i;
        while(l < 1) l += l;
        while(f < 1) f += f;
        while(d < 1) d += d;
    }

    public static void sub(byte b, short s, char c, int i, long l, float f, double d) {
        while(b < 1) b -= b;
        while(s < 1) s -= s;
        while(c < 1) c -= c;
        while(i < 1) i -= i;
        while(l < 1) l -= l;
        while(f < 1) f -= f;
        while(d < 1) d -= d;
    }

    public static void mul(byte b, short s, char c, int i, long l, float f, double d) {
        while(b < 1) b *= b;
        while(s < 1) s *= s;
        while(c < 1) c *= c;
        while(i < 1) i *= i;
        while(l < 1) l *= l;
        while(f < 1) f *= f;
        while(d < 1) d *= d;
    }

    public static void div(byte b, short s, char c, int i, long l, float f, double d) {
        while(b < 1) b /= b;
        while(s < 1) s /= s;
        while(c < 1) c /= c;
        while(i < 1) i /= i;
        while(l < 1) l /= l;
        while(f < 1) f /= f;
        while(d < 1) d /= d;
    }

    public static void rem(byte b, short s, char c, int i, long l, float f, double d) {
        while(b < 1) b %= b;
        while(s < 1) s %= s;
        while(c < 1) c %= c;
        while(i < 1) i %= i;
        while(l < 1) l %= l;
        while(f < 1) f %= f;
        while(d < 1) d %= d;
    }

    public static void and(byte b, short s, char c, int i, long l) {
        while(b < 1) b &= b;
        while(s < 1) s &= s;
        while(c < 1) c &= c;
        while(i < 1) i &= i;
        while(l < 1) l &= l;
    }

    public static void or(byte b, short s, char c, int i, long l) {
        while(b < 1) b |= b;
        while(s < 1) s |= s;
        while(c < 1) c |= c;
        while(i < 1) i |= i;
        while(l < 1) l |= l;
    }

    public static void xor(byte b, short s, char c, int i, long l) {
        while(b < 1) b ^= b;
        while(s < 1) s ^= s;
        while(c < 1) c ^= c;
        while(i < 1) i ^= i;
        while(l < 1) l ^= l;
    }

    public static void not(byte b, short s, char c, int i, long l) {
        while(b < 1) b = (byte)~b;
        while(s < 1) s = (short)~s;
        while(c < 1) c = (char)~c;
        while(i < 1) i = ~i;
        while(l < 1) l = ~l;
    }

    public static void neg(byte b, short s, char c, int i, long l) {
        while(b < 1) b = (byte)-b;
        while(s < 1) s = (short)-s;
        while(c < 1) c = (char)-c;
        while(i < 1) i = -i;
        while(l < 1) l = -l;
    }

    public static void shl(byte b, short s, char c, int i, long l) {
        while(b < 1) b <<= b;
        while(s < 1) s <<= s;
        while(c < 1) c <<= c;
        while(i < 1) i <<= i;
        while(l < 1) l <<= (int)l;
    }

    public static void shr(byte b, short s, char c, int i, long l) {
        while(b < 1) b >>= b;
        while(s < 1) s >>= s;
        while(c < 1) c >>= c;
        while(i < 1) i >>= i;
        while(l < 1) l >>= (int)l;
    }

    public static void ushr(byte b, short s, char c, int i, long l) {
        while(b < 1) b >>>= b;
        while(s < 1) s >>>= s;
        while(c < 1) c >>>= c;
        while(i < 1) i >>>= i;
        while(l < 1) l >>>= (int)l;
    }

    public static void assign(byte b, short s, char c, int i, long l, float f, double d) {
        while(b < 1) b = -1;
        while(s < 1) s = -1;
        while(c < 1) c = '\uFFFF';
        while(i < 1) i = -1;
        while(l < 1) l = -1;
        while(f < 1) f = -1;
        while(d < 1) d = -1;
    }
}
