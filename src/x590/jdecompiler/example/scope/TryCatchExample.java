package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
@SuppressWarnings("unused")
public class TryCatchExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(TryCatchExample.class);
	}
	
	public void foo() {}
	
	public void method1() {
//		try {
//			int x = 1;
//		} catch(Throwable ex) {
//			System.out.println("UUU");
//		}
		
		try {
			try {
				int i = 0;
				int j = 10 / i;
			} catch(ArithmeticException ex) {
				ex.printStackTrace();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
		} catch(Exception ex) {
			System.out.println(ex);
		}
	}
}
