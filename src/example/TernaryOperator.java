package example;

public class TernaryOperator {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(TernaryOperator.class);
	}
	
	public static boolean x = true;
	
	public static int foo() {
		return x ? 1 : 0;
	}
	
	public static boolean bar() {
		return !!x; // Декомпилирует точно так же! Я даже не предусматривал это в коде.
	}
	
	public static boolean baz1(int x) {
		if(x == 10) {
			if(x == 20)
				return true;
		}
		
		return false;
	}
	
	public static boolean baz2(int x) {
		return x == 10 && x == 20;
	}
}