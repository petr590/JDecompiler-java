package x590.jdecompiler.example.features;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;
import x590.jdecompiler.example.SuperclassExample;
import x590.jdecompiler.example.extending.SuperinterfaceExample;

@Example
public class SuperclassOmittingExample extends SuperclassExample implements SuperinterfaceExample {

	public static void main(String[] args) {
		ExampleTesting.runDecompiler(SuperclassOmittingExample.class);
	}

	@Override
	public void foo() {
		SuperinterfaceExample.super.foo();
	}

	@Override
	public int bar() {
		return SuperinterfaceExample.super.bar();
	}
}
