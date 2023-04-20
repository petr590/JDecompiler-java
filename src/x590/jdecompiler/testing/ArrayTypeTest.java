package x590.jdecompiler.testing;

import static org.junit.Assert.*;

import org.junit.Test;

import x590.jdecompiler.type.ArrayType;
import x590.jdecompiler.type.UncertainReferenceType;

public class ArrayTypeTest {
	
	@Test
	public void testArrayType() {
		assertTrue(ArrayType.STRING_ARRAY.isSubclassOf(ArrayType.OBJECT_ARRAY));
		assertTrue(ArrayType.OBJECT_ARRAY.isSubclassOf(ArrayType.ANY_ARRAY));
		
		assertTrue(UncertainReferenceType.getInstance(ArrayType.STRING_ARRAY).isSubtypeOf(ArrayType.ANY_ARRAY));
		assertTrue(ArrayType.forType(ArrayType.OBJECT_ARRAY).isSubclassOf(ArrayType.OBJECT_ARRAY));
		
		assertTrue(ArrayType.INT_ARRAY.isSubclassOf(ArrayType.ANY_ARRAY));
		
		assertTrue(ArrayType.BYTE_ARRAY.isSubclassOf(ArrayType.BYTE_OR_BOOLEAN_ARRAY));
		assertTrue(ArrayType.BOOLEAN_ARRAY.isSubclassOf(ArrayType.BYTE_OR_BOOLEAN_ARRAY));
	}
}
