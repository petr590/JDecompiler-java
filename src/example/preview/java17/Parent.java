package example.preview.java17;

import example.ExampleTesting;

public sealed class Parent permits Child1, Child2 {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Parent.class);
	}
}