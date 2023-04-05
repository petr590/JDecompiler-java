package x590.jdecompiler.example;

@Example
public class ArrayInvokeExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ArrayInvokeExample.class);
	}
	
	public Object[] foo() {
		Object[] arr = new Object[0];
		return arr.clone();
	}
}
