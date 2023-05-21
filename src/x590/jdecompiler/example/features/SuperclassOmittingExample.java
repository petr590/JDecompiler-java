package x590.jdecompiler.example.features;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;
import x590.jdecompiler.example.extending.SuperinterfaceExample;
import x590.jdecompiler.example.inheritance.SuperclassExample;

@Example
public class SuperclassOmittingExample extends SuperclassExample implements SuperinterfaceExample {

	public static void main(String[] args) {
		ExampleTesting.runDecompiler(SuperclassOmittingExample.class);
	}

	@Override
	public void foo() {
		super.classMethod1(1);
		SuperinterfaceExample.super.foo();
	}

	@Override
	public int classMethod2() {
		super.classMethod2();
		return SuperinterfaceExample.super.classMethod2();
	}
}
