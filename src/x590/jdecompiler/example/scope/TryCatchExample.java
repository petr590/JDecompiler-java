package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
@SuppressWarnings("unused")
public class TryCatchExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(TryCatchExample.class);
	}
	
//	@SuppressWarnings("finally")
	public void testFinally() {
		try {
			System.out.println("A");
		} finally {
			System.out.println("B");
		}
	}
	
	public void testTryWithResources() {
		try(var a = new AutoCloseable() { @Override public void close() {} }) {
			System.out.println("A");
		}
	}
	
	public void testTryMultiCatch() {
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
	
	public Object getObject() {
		return null;
	}
	
	public Object testTryWithReturn() {
		try {
			return getObject();
		} catch(Exception ex) {
			System.out.println();
		}
		
		return null;
	}
}
