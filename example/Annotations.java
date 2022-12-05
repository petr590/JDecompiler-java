package example;

import example.annotation.InvisibleAnnotation;
import example.annotation.VisibleAnnotation;

public class Annotations {
	
	@VisibleAnnotation
	@InvisibleAnnotation
	public static final void foo() {}
}