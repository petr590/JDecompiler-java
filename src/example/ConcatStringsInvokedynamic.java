package example;

public class ConcatStringsInvokedynamic {
	
	public static void main(String[] args) {
//		try {
//			var process = Runtime.getRuntime().exec("javac -d vbin/ -cp bin/ src/example/ConcatStringsInvokedynamic.java");
//			process.waitFor();
//		} catch(IOException | InterruptedException ex) {
//			ex.printStackTrace();
//		}
		
		ExampleTesting.runDecompiler("vbin/example/ConcatStringsInvokedynamic.class");
	}
	
	public static Object staticStrs[] = {};
	public Object nonstaticStrs[] = {};
	
	public void foo(int i, int j) {
		String str = "i: {" + i + "}; j: {" + j + "}";
		str += str;
		
		staticStrs[0]    = staticStrs[0]    + "" + i;
		nonstaticStrs[0] = nonstaticStrs[0] + "" + j;
		
//		staticStrs[0]    += "" + i;
//		nonstaticStrs[0] += "" + j;
	}
}