package x590.jdecompiler.test;

import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.GeneralCastingKind;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;

public class PrimitiveTest1 {
	
	public static void main(String[] args) {
		
		test(PrimitiveType.INT);
		test(PrimitiveType.CHAR);
		test(PrimitiveType.SHORT);
		test(PrimitiveType.BYTE);
		
		test(ClassType.INTEGER);
		test(ClassType.CHARACTER);
		test(ClassType.SHORT);
		test(ClassType.BYTE);
	}
	
	private static void test(Type type) {
		System.out.println(type.implicitCastToGeneralNoexcept(type, GeneralCastingKind.BINARY_OPERATOR));
	}
}
