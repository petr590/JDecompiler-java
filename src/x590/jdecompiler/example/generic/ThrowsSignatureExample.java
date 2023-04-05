package x590.jdecompiler.example.generic;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class ThrowsSignatureExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ThrowsSignatureExample.class);
	}
	
	public static <T extends Throwable> void foo(T t) throws T {
		throw t;
	}
	
	public static void bar(Class<?> clazz) throws IllegalArgumentException {
		
	}
}