package x590.jdecompiler.example;

@Example
public class ModifiersExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ModifiersExample.class);
	}
	
	private static final synchronized native void method1();
	private static final synchronized strictfp void method2() {}
}
