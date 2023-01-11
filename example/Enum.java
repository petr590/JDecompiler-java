package example;

public enum Enum {
	A(null, null), B(A, java.lang.Enum.class), C(new Object(), Object.class);
	
	Enum(Object obj, Class<?> clazz) {}
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Enum.class);
	}
}