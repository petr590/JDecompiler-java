package x590.jdecompiler.example;

@Example
public class ByteBooleanArrayExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ByteBooleanArrayExample.class);
	}
	
	@SuppressWarnings("null")
	public void foo() {
		byte[] arr1 = null;
		boolean[] arr2 = null;
		
		System.out.println(arr1[0]);
		System.out.println(arr2[0]);
	}
}
