package x590.jdecompiler.test;

import java.util.Map;

public class ClassNamesTest {
	
	public static void main(String[] args) {
		Class<?> clazz = Map.Entry.class;
		
		System.out.println("getName()          -> " + clazz.getName());
		System.out.println("getCanonicalName() -> " + clazz.getCanonicalName());
		System.out.println("getTypeName()      -> " + clazz.getTypeName());
		System.out.println("getSimpleName()    -> " + clazz.getSimpleName());
		System.out.println("getPackageName()   -> " + clazz.getPackageName());
		System.out.println("descriptorString() -> " + clazz.descriptorString());
	}
}
