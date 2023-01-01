package test;

import x590.javaclass.type.ClassType;
import x590.javaclass.type.Types;

public class ClassTypeTest {
	
	public static void main(String[] args) {
		
		ClassType type = ClassType.fromDescriptor("java/util/ArrayList");
		
		System.out.println(type.castToWidest(Types.ANY_OBJECT_TYPE));
	}
}