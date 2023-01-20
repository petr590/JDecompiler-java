package example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.TYPE_PARAMETER,
	ElementType.TYPE_USE, ElementType.LOCAL_VARIABLE, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE,
	ElementType.PACKAGE, ElementType.MODULE, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {
	public char value() default '\0';
}
