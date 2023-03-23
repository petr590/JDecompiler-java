package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
@SuppressWarnings("unused")
public class TryCatchFinallyExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(TryCatchFinallyExample.class);
	}
	
	public void foo() {}
	
	public void method1() {
		try {
			int i = 0;
			int j = 10 / i;
//		} catch(ArithmeticException ex) {
//			ex.printStackTrace();
//		} catch(Exception ex) {
//			ex.printStackTrace();
		} finally {
			System.out.println("Lol");
		}
	}
}
