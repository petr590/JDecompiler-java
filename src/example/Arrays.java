package example;

public class Arrays {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Arrays.class);
	}
	
	public int[] arr1      =  { 0, 0, 0, 0, 0 };
	public int[][] arr2    = {{ 0, 0, 0, 0, 0 }};
	public double[] arrd   =  { 0, 0, 0, 0, 0 };
	public String[] arr3   =  { "A", null, "null", null };
	public String[][] arr4 = {{ "A", null, java.util.Arrays.toString(((Arrays)null).getArr1()) }, null};
	
	void foo() {
		Object a = new int[30];
		
		((int[])a)[-1] = 31;
		
		Object o = "";
		((String)o).getClass();
	}

	private int[] getArr1() {
		return arr1;
	}
}