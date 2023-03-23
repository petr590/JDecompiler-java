package x590.jdecompiler.example;

@Example
public class ThrowsExample {

	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ThrowsExample.class);
	}

	public static void foo() throws Exception {}
}