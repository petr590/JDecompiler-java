package x590.jdecompiler.testing;

import x590.jdecompiler.exception.IncopatibleTypesException;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.UncertainIntegralType;

import org.junit.Test;

import static org.junit.Assert.*;
import static x590.jdecompiler.type.PrimitiveType.*;

public class TypeTest {
	
	@Test
	public void testUncertainIntegralType() {
		Type type = BYTE_SHORT_INT_CHAR_BOOLEAN;
		
		assertEquals(type.castToNarrowest(BOOLEAN), BOOLEAN);
		assertEquals(type.castToNarrowest(BYTE),    UncertainIntegralType.getInstance(1, 1));
		assertEquals(type.castToNarrowest(SHORT),   UncertainIntegralType.getInstance(1, 2));
		assertEquals(type.castToNarrowest(CHAR),    CHAR);
		assertEquals(type.castToNarrowest(INT),     UncertainIntegralType.getInstance(1, 4, UncertainIntegralType.INCLUDE_CHAR));
		
		assertEquals(type.castToWidest(BOOLEAN), BOOLEAN);
		assertEquals(type.castToWidest(BYTE),    UncertainIntegralType.getInstance(1, 4));
		assertEquals(type.castToWidest(SHORT),   UncertainIntegralType.getInstance(2, 4));
		assertEquals(type.castToWidest(CHAR),    UncertainIntegralType.getInstance(4, 4, UncertainIntegralType.INCLUDE_CHAR));
		assertEquals(type.castToWidest(INT),     UncertainIntegralType.getInstance(4, 4));
	}
	
	@Test
	public void testPrimitiveType() {
		assertThrows(IncopatibleTypesException.class, () -> BOOLEAN.castToNarrowest(BYTE));
		assertThrows(IncopatibleTypesException.class, () -> BOOLEAN.castToNarrowest(SHORT));
		assertThrows(IncopatibleTypesException.class, () -> BOOLEAN.castToNarrowest(CHAR));
		assertThrows(IncopatibleTypesException.class, () -> BOOLEAN.castToNarrowest(INT));
		
		assertThrows(IncopatibleTypesException.class, () -> BYTE.castToNarrowest(BOOLEAN));
		assertThrows(IncopatibleTypesException.class, () -> SHORT.castToNarrowest(BOOLEAN));
		assertThrows(IncopatibleTypesException.class, () -> CHAR.castToNarrowest(BOOLEAN));
		assertThrows(IncopatibleTypesException.class, () -> INT.castToNarrowest(BOOLEAN));
		
		assertThrows(IncopatibleTypesException.class, () -> BYTE.castToNarrowest(CHAR));
		assertThrows(IncopatibleTypesException.class, () -> SHORT.castToNarrowest(CHAR));
		assertThrows(IncopatibleTypesException.class, () -> INT.castToNarrowest(CHAR));
		
		assertEquals(BYTE.castToNarrowest(SHORT), BYTE);
		assertEquals(BYTE.castToNarrowest(INT), BYTE);
		
		assertThrows(IncopatibleTypesException.class, () -> SHORT.castToNarrowest(BYTE));
		assertEquals(SHORT.castToNarrowest(INT), SHORT);
		
		assertThrows(IncopatibleTypesException.class, () -> INT.castToNarrowest(BYTE));
		assertThrows(IncopatibleTypesException.class, () -> INT.castToNarrowest(SHORT));
		
		assertThrows(IncopatibleTypesException.class, () -> CHAR.castToNarrowest(BYTE));
		assertThrows(IncopatibleTypesException.class, () -> CHAR.castToNarrowest(SHORT));
		assertEquals(CHAR.castToNarrowest(INT), CHAR);
	}
}
