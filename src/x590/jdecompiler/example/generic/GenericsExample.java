package x590.jdecompiler.example.generic;

import java.util.List;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public class GenericsExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(GenericsExample.class);
	}
	
	public static <T extends Number> T foo(T t1) {
		T t2 = t1;
		return t2;
	}
	
	public static List<?> bar(List<Number> list) {
		var list2 = list;
		return list2;
	}
	
	public static Number baz(List<Number> list) {
		Number num = list.get(0);
		return num;
	}
	
	public static <T> T bak(List<T> list) {
		T t = list.get(0);
		return t;
	}
	
	public class InnerClass<T> {
		
		public InnerClass(T a, float b, double c) {}
	}
	
	/**
	 * Хак, с помощью которого можно обойти систему проверки типов исключений в Java
	 */
	public static void throwThat(Throwable exception) {
		GenericsExample.<RuntimeException>throw0(exception);
	}
	
	@SuppressWarnings("unchecked")
	private static <E extends Throwable> void throw0(Throwable exception) throws E {
		throw (E)exception;
	}
}
