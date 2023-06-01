package x590.jdecompiler.example.array;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class ArrayTypeExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ArrayTypeExample.class);
	}
	
	public Class<?>[] testClone() {
		return new Class[0].clone();
	}
}
