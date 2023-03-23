package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class IfExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(IfExample.class);
	}
	
	public static void ifScope(Object obj) {
		System.out.println("Blablabla");
		
		if(obj == null)
			System.out.println("Null");
		
		System.out.println("Blablabla");
	}
	
	public static void ifAndScope(Object obj) {
		if(obj != null && obj.getClass().toString().length() > 10)
			System.out.println(obj);
	}
	
	public static void ifOrScope(Object obj) {
		if(obj == null || obj.getClass().toString().length() <= 10)
			System.out.println(obj);
	}
	
	public static void difficultIfScope(Object obj1, Object obj2) {
		if(obj1 != null && obj2 != null || obj1 != null && obj1.getClass().toString().length() <= 10)
			System.out.println(obj1);
	}
	
	public static void difficultIfScope2(Object obj1, Object obj2, Object obj3) {
		System.out.println("Test 1");
		if(obj1 != null) {
			
			System.out.println("Test 2");
			if(obj2 != null) {
				
				System.out.println("Test 3");
				if(obj3 != null) {
					
					System.out.println("Win");
				}
			}
		}
	}
	
	public static void difficultIfScope3(Object obj1, Object obj2, Object obj3) {
		if(obj1 != null) {
			if(obj2 != null) {
				if(obj3 != null) {
					System.out.println("Win");
				}
			}
		}
	}
}
