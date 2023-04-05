package x590.jdecompiler.example.scope;

import x590.jdecompiler.example.ExampleTesting;

public class ForEachLoopExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ForEachLoopExample.class);
	}
	
	public void foo(Class<?>[] classes) {
		
		for(Class<?> clazz : classes) {
			System.out.println(clazz);
		}
	}
	
	public void bar(Iterable<Class<?>> classes) {
		
		for(Class<?> clazz : classes) {
			System.out.println(clazz);
		}
	}
}
