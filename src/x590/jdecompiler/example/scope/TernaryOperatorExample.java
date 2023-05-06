package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class TernaryOperatorExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(TernaryOperatorExample.class);
	}
	
	public static boolean x = true, y;
	
	public static int foo() {
		return x ? 1 : y ? 0 : -1;
	}
	
	public static boolean bar() {
		return !!x;
	}
	
	public static boolean baz1(int x) {
		if(x == 10) {
			if(x == 20)
				return true;
		}
		
		return false;
	}
	
	public static boolean baz2(int x) {
		return x == 10 || x == 20;
	}
	
	
	public boolean intTest(int i) {
		return i > 0x10;
	}
	
	public boolean longTest(long l) {
		return l > 0x10;
	}
	
	public boolean floatTest(float f) {
		return f > 0x10;
	}
	
	public boolean doubleTest(double d) {
		return d > 0x10;
	}
}
