package x590.jdecompiler.example.jdk;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;
import x590.jdecompiler.FileSource;

@Example(classes = Class.class, source = FileSource.JDK)
public class ClassExample {
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		ExampleTesting.runDecompilerForJdk(Class.class
				, Class.forName("java.lang.Class$1")
				, Class.forName("java.lang.Class$2")
				, Class.forName("java.lang.Class$3")
		);
		
//		ExampleTesting.runDecompiler(
//				"/home/winch/0x590/java/jdk-8-rt/java/lang/Class.class",
//				"/home/winch/0x590/java/jdk-8-rt/java/lang/Class$1.class"
//		);
//		ExampleTesting.runDecompilerForJdk(Class.class);
	}
}
