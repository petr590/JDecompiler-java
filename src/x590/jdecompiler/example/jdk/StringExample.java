package x590.jdecompiler.example.jdk;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.Example.DecompilationSource;
import x590.jdecompiler.example.ExampleTesting;

@Example(classes = String.class, source = DecompilationSource.JDK)
public class StringExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompilerForJdk(String.class);
	}
}
