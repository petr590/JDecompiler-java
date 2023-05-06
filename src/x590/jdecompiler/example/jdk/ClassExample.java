package x590.jdecompiler.example.jdk;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;
import x590.jdecompiler.example.Example.DecompilationSource;

@Example(classes = Class.class, source = DecompilationSource.JDK)
public class ClassExample {
	
	public static void main(String[] args) /* throws ClassNotFoundException */ {
		
//		ExampleTesting.runDecompilerForJdk(Class.class
//				, Class.forName("java.lang.Class$1")
//				, Class.forName("java.lang.Class$2")
//				, Class.forName("java.lang.Class$3")
//		);
		
		ExampleTesting.runDecompiler("/home/winch/0x590/java/jdk-8-rt/java/lang/Class.class");
	}
}
