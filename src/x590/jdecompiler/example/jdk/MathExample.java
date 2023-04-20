package x590.jdecompiler.example.jdk;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;
import x590.jdecompiler.example.Example.DecompilationSource;

@Example(classes = Math.class, source = DecompilationSource.JDK)
public class MathExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompilerForJdk(Math.class);
	}
}
