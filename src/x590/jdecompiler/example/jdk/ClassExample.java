package x590.jdecompiler.example.jdk;

import java.io.OutputStream;
import java.io.PrintStream;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;
import x590.jdecompiler.example.Example.DecompilationSource;

@Example(classes = Class.class, source = DecompilationSource.JDK)
public class ClassExample {
	
	public static void main(String[] args) throws ClassNotFoundException {
//		Class<?> clazz = Class.forName("java.lang.Class$1");
//		
//		Logger.debug(clazz.getTypeName());
		
		System.setOut(new PrintStream(OutputStream.nullOutputStream()));
		
		ExampleTesting.runDecompilerForJdk(Class.class
//				, Class.forName("java.lang.Class$1")
//				, Class.forName("java.lang.Class$2")
//				, Class.forName("java.lang.Class$3")
		);
	}
}
