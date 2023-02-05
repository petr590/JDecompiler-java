package example.invoke;

import example.ExampleTesting;

public class ConcatStringsInvokedynamic {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler("vbin/example/ConcatStringsInvokedynamic.class");
//		ExampleTesting.runDecompiler(ConcatStringsInvokedynamic.class);
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