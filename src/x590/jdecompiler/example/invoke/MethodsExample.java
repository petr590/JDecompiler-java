package x590.jdecompiler.example.invoke;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;
import x590.jdecompiler.example.inheritance.SuperclassExample;

@Example
public class MethodsExample extends SuperclassExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Example.class);
	}
	
	{
		classMethod1(0);
		classMethod2();
	}
	
	static {
		gg();
	}
	
	@Override
	public void classMethod1(int x) {
		super.classMethod1(x);
	}
	
	@Override
	public int classMethod2() {
		return super.classMethod2();
	}
	
	public static void gg() {}
}
