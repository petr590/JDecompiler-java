package x590.jdecompiler.example;

@Example(args = ExampleTesting.DEFAULT_DIR + "/x590/jdecompiler/example/package-info.class")
public class PackageInfoExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ExampleTesting.getClassPath(PackageInfoExample.class.getPackageName() + ".package-info"));
	}
}
