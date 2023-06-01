package x590.jdecompiler.example.other;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class CharsetExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(CharsetExample.class);
	}
	
	public static void foo() {
		System.out.println("こんにちは");
	}
}
