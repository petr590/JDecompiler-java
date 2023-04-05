package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;
import x590.jdecompiler.example.SuperclassExample;

@Example
public class LocalsExample extends SuperclassExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(LocalsExample.class);
	}
	
	public void differentVars(int x) {
		
		if(x == 0) {
			int y = 1;
			System.out.println(x + ", " + y);
		}
		
		int z = -2;
		System.out.println(x + ", 0, " + z);
	}
	
	public void sameVar(int x) {
		
		int y;
		
		if(x != 0) {
			y = x;
		} else {
			y = -1;
		}
		
		System.out.println(y);
	}
}
