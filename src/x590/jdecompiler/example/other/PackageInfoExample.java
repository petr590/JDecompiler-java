package x590.jdecompiler.example.other;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example(args = ExampleTesting.DEFAULT_DIR + "/x590/jdecompiler/example/package-info.class")
public class PackageInfoExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ExampleTesting.getClassPath(PackageInfoExample.class.getPackageName() + ".package-info"));
	}
}
