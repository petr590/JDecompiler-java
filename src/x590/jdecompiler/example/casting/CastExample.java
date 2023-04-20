package x590.jdecompiler.example.casting;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
@SuppressWarnings("unused")
public class CastExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(CastExample.class);
	}
	
	public static void foo(Object obj) {
		String str = (String)obj;
	}
}
