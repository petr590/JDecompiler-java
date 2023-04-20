package x590.jdecompiler.example.annotation;

import java.util.ArrayList;
import java.util.List;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Example
public abstract class AnnotationsExample {

	@InvisibleAnnotationExample(@VisibleAnnotationExample(value = 2, array = @IntAnnotationExample(10)))
	@RepeatableAnnotationExampleContainer({
		@RepeatableAnnotationExample(1),
		@RepeatableAnnotationExample(2)
	})
	public static void main(String[] args) {
		ExampleTesting.runDecompiler(AnnotationsExample.class);
	}
	
	
	@MethodAnnotationExample
	public abstract @TypeUseAnnotationExample int foo(
			@RepeatableAnnotationExampleContainer({
				@RepeatableAnnotationExample(1),
				@RepeatableAnnotationExample(2)
			})
			@ParameterAnnotationExample
			int value);
	
	@MethodAnnotationExample
	public static List<@TypeUseAnnotationExample String> bar(@ParameterAnnotationExample int size) {
		return new ArrayList<>(size);
	}
}
