package x590.jdecompiler.example.invoke;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example(classes = ConcatStringsExample.class, directory = ExampleTesting.VANILLA_DIR)
public class ConcatStringsStringBuilderExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ConcatStringsExample.class);
	}
}
