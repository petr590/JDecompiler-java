package example.invoke;

import example.Example;
import example.ExampleTesting;
import example.Superclass;

public class Methods extends Superclass {
	
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
	public void bar() {
		super.bar();
	}
	
	public static void gg() {}
}