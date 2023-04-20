package x590.jdecompiler.example.annotation;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@Repeatable(RepeatableAnnotationExampleContainer.class)
@MultiAnnotationExample
public @interface RepeatableAnnotationExample {
	int value();
}
