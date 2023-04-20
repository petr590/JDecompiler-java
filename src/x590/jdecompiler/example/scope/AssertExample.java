package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class AssertExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(AssertExample.class);
	}
	
	public void foo(Object obj) {
		assert obj != null : "Null";
		assert obj.getClass() == Object.class : "Not pure Object";
	}
}
