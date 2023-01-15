package example;

public class ConcatStringsInvokedynamic {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler("vbin/example/ConcatStringsInvokedynamic.class");
	}
	
	public static Comparable<String> staticStr;
	public Comparable<String> nonstaticStr;
	
	public void foo(int i, int j) {
		String str = "i: {" + i + "}; j: {" + j + "}";
		str += str;
		
		staticStr = staticStr + "" + i;
		nonstaticStr = nonstaticStr + "" + j;
	}
}