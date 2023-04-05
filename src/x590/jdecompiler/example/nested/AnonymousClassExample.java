package x590.jdecompiler.example.nested;

import java.util.ArrayList;
import java.util.List;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class AnonymousClassExample {
	
	public String name = "Anonymous class";
	
	public static void main(String[] args) /* throws ClassNotFoundException */ {
		ExampleTesting.runDecompiler(AnonymousClassExample.class//, "-A"
//				, Class.forName(AnonymousClassExample.class.getName() + "$1")
//				, Class.forName(AnonymousClassExample.class.getName() + "$2")
		);
	}
	
	public List<String> foo() {
		return new ArrayList<>(10) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public String toString() {
				return name;
			}
		};
	}
	
	public Inner bar() {
		return new Inner(1, 2, 3) {
			@Override
			public String toString() {
				return name;
			}
		};
	}
	
	public static class Inner {
		
		public Inner() {}
		
		public Inner(int arg1) {}
		
		public Inner(int... args) {}
	}
}
