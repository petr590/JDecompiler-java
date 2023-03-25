package x590.jdecompiler.example.vanilla;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example(classes = Class.class, directory = ExampleTesting.JDK_DIR)
public class ClassExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompilerForJdk(Class.class);
	}
}
