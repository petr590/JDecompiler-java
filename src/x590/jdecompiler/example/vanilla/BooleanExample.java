package x590.jdecompiler.example.vanilla;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example(classes = Boolean.class, directory = ExampleTesting.JDK_DIR)
public class BooleanExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompilerForJdk(Boolean.class);
	}
}
