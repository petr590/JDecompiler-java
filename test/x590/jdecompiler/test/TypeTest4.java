package x590.jdecompiler.test;

import x590.jdecompiler.type.ClassType;

public class TypeTest4 {
	
	public static void main(String[] args) {
		System.out.println(ClassType.fromDescriptor("java/util/List").isSubclassOf(ClassType.OBJECT));
	}
}