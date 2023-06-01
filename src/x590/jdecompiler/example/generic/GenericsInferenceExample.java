package x590.jdecompiler.example.generic;

import java.io.Serializable;
import java.util.List;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
@SuppressWarnings("unused")
public class GenericsInferenceExample {
	
	public static void main(String[] args) {
//		ExampleTesting.runDecompiler(ExampleTesting.VANILLA_DIR, GenericsInferenceExample.class);
		ExampleTesting.runDecompiler(ExampleTesting.DEFAULT_DIR, GenericsInferenceExample.class);
	}
	
	public static <T extends Number> T varTypeChecking(T t1) {
		T t2 = t1;
		return t2;
	}

	public static List<?> varTypeChecking(List<Number> list) {
		List<Number> list2 = list;
		return list2;
	}
	
	public static Number genericMethodChecking(List<Number> list) {
		Number num = list.get(0);
		return num;
	}

	public static <T> T genericMethodChecking2(List<T> list) {
		T t = list.get(0);
		return t;
	}

	public static <T> T genericMethodChecking(Class3<T> c) {
		T t = c.get();
		return t;
	}

	public static String genericFieldChecking(Class1<String> c) {
		String s = c.a;
		return s;
	}


	public static class Class1<A> {
		public A a;

		public A get() {
			return a;
		}
	}

	public static class Class2<B> extends Class1<B> {}

	public static class Class3<C> extends Class2<C> {}


//	public class InnerClass<T> {
//		
//		public InnerClass(T a, float b, double c) {}
//	}
//	
//	/**
//	 * Хак, с помощью которого можно обойти систему проверки типов исключений в Java
//	 */
//	public static void throwThat(Throwable exception) {
//		GenericsExample.<RuntimeException>throw0(exception);
//	}
//	
//	@SuppressWarnings("unchecked")
//	private static <E extends Throwable> void throw0(Throwable exception) throws E {
//		throw (E)exception;
//	}
}
