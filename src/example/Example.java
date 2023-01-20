package example;

public class Example {
	
	public static final int[][][] f = {
			{
				{1, 2, 3},
				{4, 5, 6},
				{7, 8, 9},
			},
			{
				{9, 8, 7},
				{6, 5, 4},
				{3, 2, 1},
			},
			{
				{ (int)Math.sin(Math.PI) }
			}
	};
	
	public final int i;
	
	public Example() {
		this.i = 10;
	}
	
	/*- Comment!!! */
	
	public Example(int i) {
		assert i >= 0;
		this.i = i;
		System.out.println("i+1 is " + (~i + 0b1 & i & foo(f)) * 2);
	}
	
	public static int foo(int[][][] arr) {
		return arr[0][0][0] = 10;
	}
}