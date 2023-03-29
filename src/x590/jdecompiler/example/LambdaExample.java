package x590.jdecompiler.example;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

@Example
public class LambdaExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(LambdaExample.class);
	}
	
	public static Supplier<String> stringify(int ret) {
		return () -> Integer.toString(ret);
	}
	
	public static IntFunction<String> stringifyInt() {
		return String::valueOf;
	}
	
	public static Function<Class<?>, String> stringifyClass() {
		return LambdaExample::toString;
	}
	
	public Supplier<String> stringifyThis() {
		return this::toString;
	}
	
	public static Supplier<String> stringifyObj(Object obj) {
		System.out.println(obj.getClass());
		return obj::toString;
	}
	
	public static Supplier<String> newStr() {
		return String::new;
	}
	
	public static IntFunction<String[]> newStrArray() {
		return String[]::new;
	}
	
	public static IntFunction<String[][]> newTwoDimStrArray() {
		return n -> new String[n][n];
	}
	
	public static BiIntFunction<String[][]> newStrMatrix() {
		return (n, m) -> new String[n][m];
	}
	
	public static String toString(Class<?> clazz) {
		return clazz.getCanonicalName();
	}
	
	
	@FunctionalInterface
	public static interface BiIntFunction<T> {
		T accept(int a, int b);
	}
}
