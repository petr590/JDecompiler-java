package x590.jdecompiler.example.jdk;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;
import x590.jdecompiler.FileSource;

@Example(classes = Boolean.class, source = FileSource.JDK)
public class BooleanExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompilerForJdk(Boolean.class);
	}
}
