package x590.jdecompiler.example.code.cast;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class AutounboxingExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(AutounboxingExample.class);
	}
	
	@SuppressWarnings("unused")
	public static final void foo(Byte ob, Short os, Character oc, Integer oi, Long ol, Float of, Double od, Boolean oz) {
		byte b = ob;
		short s = os;
		char c = oc;
		int i = oi;
		long l = ol;
		float f = of;
		double d = od;
		boolean z = oz;
		
		b = ob;
		s = ob;
		c = (char)(byte)ob;
		i = ob;
		l = ob;
		f = ob;
		d = ob;
		b = (byte)(short)os;
		s = os;
		c = (char)(short)os;
		i = os;
		l = os;
		f = os;
		d = os;
		b = (byte)(char)oc;
		s = (short)(char)oc;
		c = oc;
		i = oc;
		l = oc;
		f = oc;
		d = oc;
		b = (byte)(int)oi;
		s = (short)(int)oi;
		c = (char)(int)oi;
		i = oi;
		l = oi;
		f = oi;
		d = oi;
		b = (byte)(long)ol;
		s = (short)(long)ol;
		c = (char)(long)ol;
		i = (int)(long)ol;
		l = ol;
		f = ol;
		d = ol;
		b = (byte)(float)of;
		s = (short)(float)of;
		c = (char)(float)of;
		i = (int)(float)of;
		l = (long)(float)of;
		f = of;
		d = of;
		b = (byte)(double)od;
		s = (short)(double)od;
		c = (char)(double)od;
		i = (int)(double)od;
		l = (long)(double)od;
		f = (float)(double)od;
		d = od;
	}
}
