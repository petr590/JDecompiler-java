package x590.jdecompiler.example;

@Example
public class VarargsExample {

	public static void main(String[] args) {
		ExampleTesting.runDecompiler(VarargsExample.class);
	}

	void foo() {}

	void foo(double arg1) {}

	void foo(double arg1, double arg2) {}

	void foo(double arg1, double arg2, double arg3) {}

	void foo(double... args) {}

	void bar() {
		foo();
		foo(0);
		foo(0, 0);
		foo(0, 0, 0);
		foo(0, 0, 0, 0);
	}
}
