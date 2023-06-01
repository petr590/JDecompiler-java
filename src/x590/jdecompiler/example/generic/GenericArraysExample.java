package x590.jdecompiler.example.generic;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class GenericArraysExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(GenericArraysExample.class);
	}
	
	public static Class<?>[] testWildcardArray(boolean cloneArray) {
		Class<?>[] wildcardArray = getWildcardArray();
		return cloneArray ? wildcardArray.clone() : wildcardArray;
	}
	
	private static native Class<?>[] getWildcardArray();
}
