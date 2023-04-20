package x590.jdecompiler.example.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface RepeatableAnnotationExampleContainer {
	RepeatableAnnotationExample[] value();
}
