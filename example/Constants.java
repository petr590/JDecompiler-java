package example;

import example.annotation.DoubleAnnotation;

public class Constants {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Constants.class);
	}
	
	public static final double PI = 3.14159;
	
	@DoubleAnnotation(PI)
	public static void foo() {
		System.out.println(PI);
	}
}