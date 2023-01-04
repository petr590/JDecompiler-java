package example;

public class ArrayInvoke {
	
	public static void main(String[] args) {
		System.out.println(java.util.Arrays.toString(Object[].class.getDeclaredMethods()));
		//ExampleTesting.runDecompiler(ArrayInvoke.class);
	}
	
	public void foo() {
		Object[] arr = new Object[] {};
		
		arr.toString();
	}
}
