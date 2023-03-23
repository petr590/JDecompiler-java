package x590.jdecompiler.example.annotation;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({FIELD, METHOD, TYPE, TYPE_PARAMETER, TYPE_USE, LOCAL_VARIABLE, CONSTRUCTOR, ANNOTATION_TYPE, PACKAGE, MODULE, RECORD_COMPONENT})
@Retention(RetentionPolicy.CLASS)
public @interface InvisibleAnnotationExample {
	public VisibleAnnotationExample value() default @VisibleAnnotationExample;
}