package x590.jdecompiler.example.nested;

import java.util.Collections;
import java.util.Map;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class NestedClassExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(NestedClassExample.class);
	}
	
	public class NonstaticInner {
		public NonstaticInner() {
			this(Collections.emptyMap());
		}
		
		public NonstaticInner(Map<String, Integer> m) {}
	}
	
	public class NonstaticInnerChild extends NonstaticInner {
		public NonstaticInnerChild() {}
		public NonstaticInnerChild(Map<String, Integer> m) {
			super(m);
		}
	}
	
	public static class StaticInner {
		public StaticInner() {}
		public StaticInner(Map<String, Integer> m) {}
	}
	
	public enum InnerEnum {
		A, B, C
	}
	
	public interface InnerInterface {}
}
