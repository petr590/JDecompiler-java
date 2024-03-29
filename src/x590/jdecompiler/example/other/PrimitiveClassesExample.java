package x590.jdecompiler.example.other;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class PrimitiveClassesExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(PrimitiveClassesExample.class);
	}
	
	public static void foo() {
		System.out.println(byte.class);
		System.out.println(short.class);
		System.out.println(char.class);
		System.out.println(int.class);
		System.out.println(long.class);
		System.out.println(float.class);
		System.out.println(double.class);
		System.out.println(boolean.class);
		System.out.println(void.class);
	}
}
