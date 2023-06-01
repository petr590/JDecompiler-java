package x590.jdecompiler.test;

import x590.jdecompiler.type.reference.ClassType;

public class ClassTypeTableTest {
	
	public static void main(String[] args) throws ClassNotFoundException {
//		for(var entry : ClassType.classTypes().entrySet())
//			System.out.printf("%-40s %s\n", entry.getKey(), entry.getValue());
//
//		System.out.println();
//
//		for(var classType : ClassType.allClassTypes())
//			System.out.println(classType);
//
//		System.out.println();
		
		
		print(ClassType.fromDescriptor("x590/jdecompiler/test/ClassTypeTest$1"));
		System.out.println();
		
		var clazz = Class.forName("x590.jdecompiler.package-info");
		
		print(ClassType.fromClass(clazz));
		System.out.println(clazz);
	}
	
	private static void print(ClassType classType) {
		System.out.println(classType.getName());
		System.out.println(classType.getEncodedName());
		System.out.println(classType.getClassEncodedName());
		System.out.println(classType.getSimpleName());
		System.out.println(classType.getPackageName());
		System.out.println(classType.getFullSimpleName());
	}
}
