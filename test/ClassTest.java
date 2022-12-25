package test;

import java.util.Arrays;

public class ClassTest {
	
	public static void main(String[] args) {
		
		try {
			System.out.println(Arrays.toString(Class.forName("java.util.LinkedList").getInterfaces()));
		} catch(ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	
	public static class TestInnerClass {}
}