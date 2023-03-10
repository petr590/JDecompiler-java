package x590.jdecompiler.example;

public class ThrowsSignature {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ThrowsSignature.class);
	}
	
	public static <T extends Throwable> void foo(T t) throws T {
		throw t;
	}
}