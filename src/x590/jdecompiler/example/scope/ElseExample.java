package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class ElseExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ElseExample.class);
	}
	
	public static void foo(int x) {
		
		if(x == 0)
			System.out.println("Zero");
		else
			System.out.println("Not zero");
	}
}
