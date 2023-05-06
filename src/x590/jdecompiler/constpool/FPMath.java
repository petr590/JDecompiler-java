package x590.jdecompiler.constpool;

import x590.jdecompiler.field.FieldDescriptor;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.type.reference.ClassType;

final class FPMath {
	
	private FPMath() {}
	
	static final ClassType MATH_CLASS = ClassType.fromClass(Math.class);
	
	static final FieldDescriptor
			PI_DESCRIPTOR = FieldDescriptor.of(PrimitiveType.DOUBLE, MATH_CLASS, "PI"),
			E_DESCRIPTOR  = FieldDescriptor.of(PrimitiveType.DOUBLE, MATH_CLASS, "E");
}
