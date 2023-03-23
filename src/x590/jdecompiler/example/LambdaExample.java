package x590.jdecompiler.example;

import java.util.function.Function;
import java.util.function.Supplier;

@Example
public class LambdaExample {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(LambdaExample.class);
	}
	
	public static Function<String, String> functionOf(int ret) {
		return s -> s + ret;
	}
	
	public static Supplier<String> supplierOf(int ret) {
		return () -> Integer.toString(ret);
	}
}
