package x590.jdecompiler.example.invoke;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class ConcatStringsInvokedynamicExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler("vbin", ConcatStringsInvokedynamicExample.class);
//		ExampleTesting.runDecompiler(ConcatStringsInvokedynamicExample.class);
	}
	
	public static Object staticStrs[] = {};
	public Object nonstaticStrs[] = {};
	
	public void foo(int i, int j) {
		String str = "i: {" + i + "}; j: {" + j + "}";
		str += str;
		
		staticStrs[0]    = staticStrs[0]    + "" + i;
		nonstaticStrs[0] = nonstaticStrs[0] + "" + j;
		
		staticStrs[0]    += "" + i;
		nonstaticStrs[0] += "" + j;
	}
}
