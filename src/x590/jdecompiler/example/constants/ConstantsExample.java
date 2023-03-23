package x590.jdecompiler.example.constants;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class ConstantsExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(ConstantsExample.class,
//				"-c=never",
				"--double-suffix",
				"--");
	}
	
	public static void foo() {
		System.out.println(Integer.MIN_VALUE);
		System.out.println(Integer.MAX_VALUE);
		
		System.out.println(Long.MIN_VALUE);
		System.out.println(Long.MAX_VALUE);
		
		System.out.println(Float.MIN_VALUE);
		System.out.println(Float.MAX_VALUE);
		System.out.println(Float.MIN_NORMAL);
		
		System.out.println(Double.MIN_VALUE);
		System.out.println(Double.MAX_VALUE);
		System.out.println(Double.MIN_NORMAL);
		
		System.out.println(Float.POSITIVE_INFINITY);
		System.out.println(Float.NEGATIVE_INFINITY);
		System.out.println(Float.NaN);
		
		System.out.println(Double.POSITIVE_INFINITY);
		System.out.println(Double.NEGATIVE_INFINITY);
		System.out.println(Double.NaN);
		
		System.out.println(Math.PI);
		System.out.println(Math.E);
		
		System.out.println((float)Math.PI);
		System.out.println((float)Math.E);
		
		
		System.out.println(-Integer.MIN_VALUE); // == Integer.MIN_VALUE
		System.out.println(-Integer.MAX_VALUE);
		
		System.out.println(-Long.MIN_VALUE); // == Long.MIN_VALUE
		System.out.println(-Long.MAX_VALUE);
		
		System.out.println(-Float.MIN_VALUE);
		System.out.println(-Float.MAX_VALUE);
		System.out.println(-Float.MIN_NORMAL);
		
		System.out.println(-Double.MIN_VALUE);
		System.out.println(-Double.MAX_VALUE);
		System.out.println(-Double.MIN_NORMAL);
		
		System.out.println(-Math.PI);
		System.out.println(-Math.E);
		
		System.out.println((float)-Math.PI);
		System.out.println((float)-Math.E);
		
		
		System.out.println((Double)Double.NaN);
		System.out.println((Float)Float.NaN);
	}
}
