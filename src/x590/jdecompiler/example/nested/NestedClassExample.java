package x590.jdecompiler.example.nested;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class NestedClassExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(NestedClassExample.class);
	}
	
	public class NonstaticInner {
		public NonstaticInner() {
			this(-1);
		}
		
		public NonstaticInner(int x) {}
	}
	
	public class NonstaticInnerChild extends NonstaticInner {
		public NonstaticInnerChild() {}
		public NonstaticInnerChild(int x) {
			super(x);
		}
	}
	
	public static class StaticInner {
		public StaticInner() {}
		public StaticInner(int x) {}
	}
	
	public enum InnerEnum {
		A, B, C
	}
	
	public interface InnerInterface {}
}
