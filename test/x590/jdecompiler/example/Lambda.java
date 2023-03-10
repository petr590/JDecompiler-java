package x590.jdecompiler.example;

import java.util.function.Function;
import java.util.function.Supplier;

public class Lambda {
	
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(Lambda.class);
	}
	
	public static Function<String, String> functionOf(int ret) {
		return s -> s + ret;
	}
	
	public static Supplier<String> supplierOf(int ret) {
		return () -> Integer.toString(ret);
	}
}
