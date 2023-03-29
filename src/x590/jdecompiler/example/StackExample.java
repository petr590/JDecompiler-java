package x590.jdecompiler.example;

@Example
public class StackExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(StackExample.class
//				, "--no-decompile-string-builder-as-concatenation"
		);
	}
	
	public void foo(int x, int y) {
		throw new IllegalArgumentException("#" + (x <= 0 ? "a" : "b"));
	}
}
