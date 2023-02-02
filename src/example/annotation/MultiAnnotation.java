package example.annotation;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({FIELD, METHOD, TYPE, TYPE_PARAMETER, TYPE_USE, LOCAL_VARIABLE, CONSTRUCTOR, ANNOTATION_TYPE, PACKAGE, MODULE, RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiAnnotation {
	public byte byteValue() default 0;
	public short shortValue() default 0;
	public char charValue() default 0;
	public int intValue() default 0;
	public long longValue() default 0;
	public float floatValue() default 0;
	public double doubleValue() default 0;
	public boolean booleanValue() default false;
	public String stringValue() default "";
}