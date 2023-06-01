package x590.jdecompiler.example.other;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class ThrowsExample {

	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ThrowsExample.class);
	}

	public static void foo() throws Exception {}
}