package x590.jdecompiler.example.other;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
@SuppressWarnings("unused")
public class ModifiersExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ModifiersExample.class);
	}
	
	private static final synchronized native void method1();
	private static final synchronized strictfp void method2() {}
}
