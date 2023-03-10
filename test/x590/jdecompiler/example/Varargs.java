package x590.jdecompiler.example;

public class Varargs {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Varargs.class);
	}
	
	void foo() {}
	
	void foo(double arg1) {}
	
	void foo(double arg1, double arg2) {}
	
	void foo(double arg1, double arg2, double arg3) {}
	
	void foo(double... args) {}
	
	void bar() {
		foo(0, 0, 0, 0);
	}
}