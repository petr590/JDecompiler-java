package x590.jdecompiler.example;

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
