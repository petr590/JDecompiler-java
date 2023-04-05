package x590.jdecompiler.example.nested;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class OuterExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(OuterExample.class);
	}
	
	static class Middle {
		static class Inner {}
	}
}
