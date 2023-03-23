package x590.jdecompiler.example.generic;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class GenericsExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(GenericsExample.class);
	}
	
	public static <T> T foo(T t) {
		T t2 = t;
		return t2;
	}
}
