package x590.jdecompiler.example;

@Example
@SuppressWarnings({ "null", "unused" })
public class ByteBooleanArrayExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ByteBooleanArrayExample.class);
	}
	
	public void foo() {
		byte[] arr1 = null;
		boolean[] arr2 = null;
		
		System.out.println(arr1[0]);
		System.out.println(arr2[0]);
	}
	
	public void bar() {
		// null нужен, чтобы декомпилятор не мог сразу понять, какой тип у массива - byte или boolean
		byte[] arr1 = null;
		byte[] arr2 = null;
		
		arr1[0] = 0;
		arr2[0] = 1;
		arr2[0] = 2;
	}
	
	public void baf(boolean b, boolean c) {
		
		boolean y = !b && c;
		
//		int x = b ? 1 : 0;
//		
//		if(x == 1) {
//			x = 0;
//		}
	}
	
	public void baz() {
		String[] arr1 = null;
		//arr1[0] = "";
		System.out.println(arr1[0]);
	}
}
