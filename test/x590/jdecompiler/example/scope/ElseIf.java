package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.ExampleTesting;

public class ElseIf {

	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ElseIf.class);
	}
	
	public static void elseIf() {
		int x = -1;
		
		if(x == 1) {
			System.out.println("1");
		} else if(x == 2) {
			System.out.println("2");
		} else if(x == 3) {
			System.out.println("3");
		} else if(x == 4) {
			System.out.println("4");
		} else if(x == 5) {
			System.out.println("5");
		} else if(x == 6) {
			System.out.println("6");
		} else if(x == 7) {
			System.out.println("7");
		} else {
			System.out.println("!");
		}
	}

}
