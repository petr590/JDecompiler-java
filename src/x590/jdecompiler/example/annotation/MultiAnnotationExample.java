package x590.jdecompiler.example.annotation;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import x590.jdecompiler.example.Example;
import x590.jdecompiler.example.ExampleTesting;

@Target({FIELD, METHOD, TYPE, TYPE_PARAMETER, TYPE_USE, LOCAL_VARIABLE, CONSTRUCTOR, ANNOTATION_TYPE, PACKAGE, MODULE, RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
@Example
public @interface MultiAnnotationExample {
	
	byte byteValue() default 0;
	short shortValue() default 0;
	char charValue() default 0;
	int intValue() default 0;
	long longValue() default 0;
	float floatValue() default 0;
	double doubleValue() default 0;
	boolean booleanValue() default false;
	String stringValue() default "";
	EnumExample enumValue() default EnumExample.A;
	Class<?> classValue() default String.class;
	AnnotationExample annotationValue() default @AnnotationExample;
	
	byte[] byteArray() default 0;
	short[] shortArray() default 0;
	char[] charArray() default 0;
	int[] intArray() default 0;
	long[] longArray() default 0;
	float[] floatArray() default 0;
	double[] doubleArray() default 0;
	boolean[] booleanArray() default false;
	String[] stringArray() default "";
	EnumExample[] enumArray() default EnumExample.A;
	Class<?>[] classArray() default String.class;
	AnnotationExample[] annotationArray() default @AnnotationExample;
	
	@interface AnnotationExample {}
	
	enum EnumExample {
		A
	}
	
	static class Inner {
		
		public static void main(String[] args) {
			ExampleTesting.runDecompiler(MultiAnnotationExample.class);
		}
	}
}