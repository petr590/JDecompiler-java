package example;

public class Loop {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Loop.class);
	}
	
	public static void foo() {
		for(int i = 0; i < 10; i++)
			System.out.println(i);
	}
}