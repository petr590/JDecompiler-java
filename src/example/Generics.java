package example;

public class Generics {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Generics.class);
	}
	
	public static <T> T foo(T t) {
		T t2 = t;
		return t2;
	}
}