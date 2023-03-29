package x590.jdecompiler.example;

@Example(classes = { OuterExample.class, OuterExample.Middle.class, OuterExample.Middle.Inner.class })
public class OuterExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(OuterExample.class,
				"bin/x590/jdecompiler/example/OuterExample$Middle.class",
				"bin/x590/jdecompiler/example/OuterExample$Middle$Inner.class");
	}
	
	static class Middle {
		static class Inner {}
	}
}
