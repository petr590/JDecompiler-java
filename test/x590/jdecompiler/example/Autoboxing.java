package x590.jdecompiler.example;

public class Autoboxing {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Autoboxing.class, "--no-trailing-zero");
	}
	
	@SuppressWarnings("unused")
	public static final void foo() {
		Byte b = 0;
		Short s = 0;
		Character c = 0;
		Integer i = 0;
		Long l = 0l;
		Float f = 0f;
		Double d = 0d;
		Boolean z = false;
	}
}