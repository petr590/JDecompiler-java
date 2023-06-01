package x590.jdecompiler.example.other;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

@Example
public class LambdaExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(LambdaExample.class);
	}
	
	public static Supplier<ClassLoader> getClassLoader(Module module) {
		System.out.println((Supplier<ClassLoader>)module::getClassLoader);
		return module::getClassLoader;
	}
	
	public static Supplier<String> stringify(int x) {
		return () -> Integer.toString(x);
	}
	
	public static Supplier<Object> stringify(Object x) {
		return x::toString;
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
	
	
	public static Runnable bar(Object x, boolean z) {
		return () -> {
			if(z) {
				System.out.println(x);
			}
		};
	}
	
	public static Runnable throwThat(RuntimeException ex) {
		return () -> {
			throw ex;
		};
	}
	
	public static String invokeLambda(boolean x) {
		return ((Supplier<String>)(() -> x ? "true" : "false")).get();
	}
	
	public static Supplier<String> lambdaForLambda(boolean x) {
		return ((Supplier<String>)(() -> x ? "true" : "false"))::get;
	}
	
	public static Supplier<String> lambdaInTernary(boolean x) {
		return x ? () -> "true" : () -> "false";
	}
	
	public static Supplier<String> lambdaInTernary2(boolean x) {
		return x ? Boolean.TRUE::toString : Boolean.FALSE::toString;
	}
	
	public static IntFunction<Object[]> lambdaInTernary3(boolean x) {
		return x ? String[]::new : Object[]::new;
	}
	
	public static Supplier<String> lambdaNative() {
		return LambdaExample::getString;
	}
	
	private static String getString() {
		return "";
	}
	
	public static String toString(Class<?> clazz) {
		return clazz.getCanonicalName();
	}
	
	
	@FunctionalInterface
	public interface BiIntFunction<T> {
		T accept(int a, int b);
	}
}
