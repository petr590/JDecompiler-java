package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example(directory = ExampleTesting.VANILLA_DIR)
public class Bug1Example {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ExampleTesting.VANILLA_DIR, Bug1Example.class);
	}
	
	public Object newReflectionData(boolean x) {
		if(!x) {
			return null;
		}
		
		while(true) {
			foo();
			
			var o = new Object();
			
			if(x) {
				return o;
			}
		}
	}
	
	private static void foo() {}
}
