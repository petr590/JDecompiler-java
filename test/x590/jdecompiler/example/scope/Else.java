package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.ExampleTesting;

public class Else {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Else.class);
	}
	
	public static void foo(int x) {
		
		if(x == 0)
			System.out.println("Zero");
		else
			System.out.println("Not zero");
	}
}