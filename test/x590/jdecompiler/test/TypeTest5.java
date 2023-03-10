package x590.jdecompiler.test;

import x590.jdecompiler.type.PrimitiveType;

public class TypeTest5 {
	
	public static void main(String[] args) {
		System.out.println(PrimitiveType.BYTE.implicitCastStatus(PrimitiveType.INT));
	}
}