package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.ExampleTesting;

public class IfElse {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(IfElse.class);
	}
	
	public static void ifElseScope(Object obj) {
		if(obj == null)
			System.out.println("Null");
		else
			System.out.println("Not null");
	}
	
	public static void difficultIfElseScope1(int x) {
		if(x == 10) {
			if(x == 20) {
				System.out.print("1");
			} else {
				System.out.print("2");
			}
			
		} else {
			System.out.print("0");
		}
	}
	
//	public static void difficultIfElseScope2(int x) {
//		if(x == 10) {
//			if(x == 20) {
//				if(x == 30) {
//					if(x == 40) {
//						System.out.print("1");
//					}
//				}
//			}
//			
//		} else {
//			System.out.print("0");
//		}
//	}
//	
//	public static void difficultIfElseScope3(Object obj1, Object obj2, Object obj3) {
//		System.out.println("Test 1");
//		if(obj1 != null) {
//			
//			System.out.println("Test 2");
//			if(obj2 != null) {
//				
//				System.out.println("Test 3");
//				if(obj3 != null) {
//					
//					System.out.println("Win");
//				} else {
//					System.out.println("Def 3");
//				}
//				
//			} else {
//				System.out.println("Def 2");
//			}
//			
//		} else {
//			System.out.println("Def 1");
//		}
//	}
}