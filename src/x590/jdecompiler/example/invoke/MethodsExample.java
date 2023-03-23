package x590.jdecompiler.example.invoke;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;
import x590.jdecompiler.example.SuperclassExample;

@Example
public class MethodsExample extends SuperclassExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Example.class);
	}
	
	{
		foo();
		bar();
	}
	
	static {
		gg();
	}
	
	@Override
	public void foo() {
		super.foo();
	}
	
	@Override
	public int bar() {
		return super.bar();
	}
	
	public static void gg() {}
}
