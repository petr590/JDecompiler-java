package x590.jdecompiler.example.invoke;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class BridgeMethodExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(BridgeMethodExample.class, "-b");
	}
	
	public class Child extends Parent {
		
		@Override
		public String getState() {
			return "";
		}
	}
	
	public class Parent {
		
		public Object getState() {
			return new Object();
		}
	}
}
