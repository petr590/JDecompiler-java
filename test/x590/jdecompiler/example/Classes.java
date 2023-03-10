package x590.jdecompiler.example;

public class Classes {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Classes.class);
	}
	
	public static void foo() {
		System.out.println(byte.class);
		System.out.println(short.class);
		System.out.println(char.class);
		System.out.println(int.class);
		System.out.println(long.class);
		System.out.println(float.class);
		System.out.println(double.class);
		System.out.println(boolean.class);
		System.out.println(void.class);
	}
}