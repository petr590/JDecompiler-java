package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class ConditionExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ConditionExample.class);
	}
	
	public static void foo(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f) {
		System.out.println("Blablabla");
		
		if((a || b) && (c || d && e || f))
			System.out.println("Null");
		
		System.out.println("Blablabla");
	}
}
