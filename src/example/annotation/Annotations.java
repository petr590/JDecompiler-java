package example.annotation;

import java.util.ArrayList;
import java.util.List;

import example.ExampleTesting;

public class Annotations {

	@InvisibleAnnotation(@VisibleAnnotation(value = 2, array = @IntAnnotation(10)))
	public static void main(String[] args) {
		ExampleTesting.runDecompiler("vbin", Annotations.class);
		
//		ExampleTesting.runDecompiler(VisibleAnnotation.class);
	}
	
	
	@MethodAnnotation
	public static @TypeUseAnnotation int foo(@ParameterAnnotation int value) {
		return value;
	}
	
	@MethodAnnotation
	public static List<@TypeUseAnnotation String> bar(@ParameterAnnotation int size) {
		return new ArrayList<>(size);
	}
}
