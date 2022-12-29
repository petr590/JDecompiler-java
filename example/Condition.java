package example;

public class Condition {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Condition.class);
	}
	
	public static void foo(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f) {
		System.out.println("Blablabla");
		
		a = b ^ c;
		
		if((a || b) && (c || d && e || f))
			System.out.println("Null");
		
		System.out.println("Blablabla");
	}
	
	public static void foo(int a, int b) {
		int c = a & b;
	}
}