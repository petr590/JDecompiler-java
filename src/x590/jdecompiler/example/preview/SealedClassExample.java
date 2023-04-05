package x590.jdecompiler.example.preview;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example(args = {
		"vbin/x590/jdecompiler/example/preview/SealedParent.class",
		"vbin/x590/jdecompiler/example/preview/SealedChild1.class",
		"vbin/x590/jdecompiler/example/preview/SealedChild2.class"
})
public class SealedClassExample {

	public static void main(String[] args) {
		ExampleTesting.runDecompiler(
				"vbin/x590/jdecompiler/example/preview/SealedParent.class",
				"vbin/x590/jdecompiler/example/preview/SealedChild1.class",
				"vbin/x590/jdecompiler/example/preview/SealedChild2.class"
		);
	}
}
