package x590.jdecompiler.example;

@Example
public class MultiDeclarationExample {
	
	// Must be inlined
	public static int x1, y1, z1;
	
	// Must not be inlined
	public static int x2 = 0, y2 = 0, z2 = 0;
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(MultiDeclarationExample.class);
	}
}
