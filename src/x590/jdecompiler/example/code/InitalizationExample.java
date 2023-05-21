package x590.jdecompiler.example.code;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class InitalizationExample {
	
	public static final int CONST1, CONST2;
	
	static {
		if(Math.random() > 0.5)
			CONST1 = 1;
		else
			CONST1 = 0;
		
		CONST2 = Math.random() > 0.5 ? 1 : 0;
	}
	
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(InitalizationExample.class);
	}
}
