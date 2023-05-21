package x590.jdecompiler.testing;

import static org.junit.Assert.*;
import static x590.jdecompiler.type.reference.ArrayType.*;

import org.junit.Test;

import x590.jdecompiler.type.UncertainReferenceType;
import x590.jdecompiler.type.reference.ArrayType;

public final class ArrayTypeTest {
	
	@Test
	public void testArrayTypes() {
		assertTrue(STRING_ARRAY.isDefinitelySubclassOf(OBJECT_ARRAY));
		assertTrue(OBJECT_ARRAY.isDefinitelySubclassOf(ANY_ARRAY));
		
		assertTrue(UncertainReferenceType.getInstance(STRING_ARRAY).isDefinitelySubtypeOf(ANY_ARRAY));
		assertTrue(ArrayType.forType(OBJECT_ARRAY).isDefinitelySubclassOf(OBJECT_ARRAY));
		
		assertTrue(INT_ARRAY.isDefinitelySubclassOf(ANY_ARRAY));
		
		assertTrue(BYTE_ARRAY.isDefinitelySubclassOf(BYTE_OR_BOOLEAN_ARRAY));
		assertTrue(BOOLEAN_ARRAY.isDefinitelySubclassOf(BYTE_OR_BOOLEAN_ARRAY));
		
		assertEquals(BYTE_ARRAY,    BYTE_OR_BOOLEAN_ARRAY.castToNarrowestNoexcept(BYTE_ARRAY));
		assertEquals(BOOLEAN_ARRAY, BYTE_OR_BOOLEAN_ARRAY.castToNarrowestNoexcept(BOOLEAN_ARRAY));
		assertEquals(BYTE_ARRAY,    BYTE_ARRAY.castToNarrowestNoexcept(BYTE_OR_BOOLEAN_ARRAY));
		assertEquals(BOOLEAN_ARRAY, BOOLEAN_ARRAY.castToNarrowestNoexcept(BYTE_OR_BOOLEAN_ARRAY));
	}
}
