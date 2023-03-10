package x590.jdecompiler.example;

public class Arrays {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Arrays.class);
	}
	
	public int[] arr1      =  { 0, 0, 0, 0, 0 };
	public int[][] arr2    = {{ 0, 0, 0, 0, 0 }};
	public double[] arrd   =  { 0, 0, 0, 0, 0 };
	public String[] arr3   =  { "A", null, "null", null };
	public String[][] arr4 = {{ "A", null, "null" }, null};
}