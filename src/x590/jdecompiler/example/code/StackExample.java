package x590.jdecompiler.example.code;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class StackExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(StackExample.class);
	}
	
	// Баг с пустым стеком, исправлено
	public void foo(int x, int y) {
		throw new IllegalArgumentException("#" + (x <= 0 ? "a" : "b"));
	}
}
