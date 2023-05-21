package x590.jdecompiler.example.code;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class InstanceofExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(InstanceofExample.class);
	}
	
	public void foo(CharSequence seq) {
		System.out.println(seq instanceof String);
	}
}
