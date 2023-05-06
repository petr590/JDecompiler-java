package x590.jdecompiler.testing;

import static org.junit.Assert.*;

import org.junit.Test;

import x590.jdecompiler.type.UncertainReferenceType;
import x590.jdecompiler.type.reference.ArrayType;

public class ArrayTypeTest {
	
	@Test
	public void testArrayType() {
		assertTrue(ArrayType.STRING_ARRAY.isDefinitelySubclassOf(ArrayType.OBJECT_ARRAY));
		assertTrue(ArrayType.OBJECT_ARRAY.isDefinitelySubclassOf(ArrayType.ANY_ARRAY));
		
		assertTrue(UncertainReferenceType.getInstance(ArrayType.STRING_ARRAY).isSubtypeOf(ArrayType.ANY_ARRAY));
		assertTrue(ArrayType.forType(ArrayType.OBJECT_ARRAY).isDefinitelySubclassOf(ArrayType.OBJECT_ARRAY));
		
		assertTrue(ArrayType.INT_ARRAY.isDefinitelySubclassOf(ArrayType.ANY_ARRAY));
		
		assertTrue(ArrayType.BYTE_ARRAY.isDefinitelySubclassOf(ArrayType.BYTE_OR_BOOLEAN_ARRAY));
		assertTrue(ArrayType.BOOLEAN_ARRAY.isDefinitelySubclassOf(ArrayType.BYTE_OR_BOOLEAN_ARRAY));
	}
}
