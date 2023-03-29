package x590.jdecompiler.constpool;

import x590.jdecompiler.field.FieldDescriptor;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;

final class FPMath {
	
	private FPMath() {}
	
	static final ClassType MATH_CLASS = ClassType.fromClass(Math.class);
	
	static final FieldDescriptor
			PI_DESCRIPTOR = new FieldDescriptor(MATH_CLASS, "PI", PrimitiveType.DOUBLE),
			E_DESCRIPTOR = new FieldDescriptor(MATH_CLASS, "E", PrimitiveType.DOUBLE);
}
