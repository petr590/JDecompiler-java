package x590.jdecompiler.example.array;

import static x590.jdecompiler.example.array.VarargsSuperclass.*;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class VarargsExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(VarargsSuperclass.class, VarargsExample.class);
	}
	
	void bar() throws NoSuchMethodException, SecurityException {
		foo();
		foo(0);
		foo(0, 0);
		foo(0, 0, 0);
		foo(0, 0, 0, 0);
		foo2("foo");
		foo2("foo", Object.class.getDeclaredMethod("equals", Object.class));
		foo2("foo", Object.class.getDeclaredMethods());
//		foo3("foo", new java.lang.reflect.Method[] {});
//		foo3("foo", new java.lang.reflect.Method[] { null });
	}
}
