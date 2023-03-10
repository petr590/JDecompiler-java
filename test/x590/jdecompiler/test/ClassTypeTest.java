package x590.jdecompiler.test;

import x590.jdecompiler.type.ClassType;

public class ClassTypeTest {
	
	public static void main(String[] args) {
		ClassType type = ClassType.fromDescriptor("java/lang/Number<Ljava/lang/Double;>K");
		
		System.out.println(type);
	}
}