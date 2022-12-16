package x590.test;

import x590.javaclass.type.PrimitiveType;

public class TypeTest3 {
	
	public static void main(String[] args) {
		System.out.println(PrimitiveType.BYTE_SHORT_CHAR_INT_BOOLEAN.castToNarrowest(PrimitiveType.BYTE));
	}
}