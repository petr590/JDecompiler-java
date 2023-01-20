package x590.test;

import x590.jdecompiler.type.PrimitiveType;

public class TypeTest3 {
	
	public static void main(String[] args) {
		System.out.println(PrimitiveType.BYTE_SHORT_INT_CHAR_BOOLEAN.castToNarrowest(PrimitiveType.BYTE));
	}
}