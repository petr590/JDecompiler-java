package x590.jdecompiler.example.array;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class ArrayInvokeExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ArrayInvokeExample.class);
	}
	
	public Object[] foo() {
		Object[] arr = new Object[0];
		return arr.clone();
	}
}
