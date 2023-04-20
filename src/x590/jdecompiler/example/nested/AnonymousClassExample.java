package x590.jdecompiler.example.nested;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class AnonymousClassExample {
	
	public String name = "Anonymous class";
	
	public void foo() {}
	
	public static void main(String[] args) /* throws ClassNotFoundException */ {
		ExampleTesting.runDecompiler(AnonymousClassExample.class//, "-A"
//				, Class.forName(AnonymousClassExample.class.getName() + "$1")
//				, Class.forName(AnonymousClassExample.class.getName() + "$2")
		);
	}
	
	public Inner bar() {
		return new Inner(1, 2, 3) {
			
			@Override
			public String getName() {
				foo();
				return name;
			}
			
			@SuppressWarnings("unused")
			public String getName(int this$0) {
				foo();
				return name;
			}
			
//			@Override public String toString() {
//				
//				return new Inner() {
//					@Override public String getName() {
//						return name;
//					}
//				}.toString();
//			}
		};
	}
	
	public static abstract class Inner {
		
		public Inner() {}
		
		public Inner(int arg1) {}
		
		public Inner(int... args) {}
		
		public abstract String getName();
	}
}
