package x590.jdecompiler.example.jdk;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;
import x590.jdecompiler.example.Example.DecompilationSource;

@Example(classes = Void.class, source = DecompilationSource.JDK)
public class VoidExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompilerForJdk(Void.class);
	}
}
