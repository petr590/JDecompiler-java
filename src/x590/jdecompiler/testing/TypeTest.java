package x590.jdecompiler.testing;

import org.junit.Test;

import junit.framework.TestCase;
import x590.jdecompiler.exception.IncopatibleTypesException;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.UncertainIntegralType;

import static org.junit.Assert.assertThrows;
import static x590.jdecompiler.type.PrimitiveType.*;

public class TypeTest extends TestCase {
	
	@Test
	public void testUncertainIntegralType() {
		Type type = PrimitiveType.BYTE_SHORT_INT_CHAR_BOOLEAN;
		
		assertEquals(type.castToNarrowest(PrimitiveType.BOOLEAN), PrimitiveType.BOOLEAN);
		assertEquals(type.castToNarrowest(PrimitiveType.BYTE),    UncertainIntegralType.getInstance(1, 1));
		assertEquals(type.castToNarrowest(PrimitiveType.SHORT),   UncertainIntegralType.getInstance(1, 2));
		assertEquals(type.castToNarrowest(PrimitiveType.CHAR),    PrimitiveType.CHAR);
		assertEquals(type.castToNarrowest(PrimitiveType.INT),     UncertainIntegralType.getInstance(1, 4, UncertainIntegralType.INCLUDE_CHAR));
		
		assertEquals(type.castToWidest(PrimitiveType.BOOLEAN), PrimitiveType.BOOLEAN);
		assertEquals(type.castToWidest(PrimitiveType.BYTE),    UncertainIntegralType.getInstance(1, 4));
		assertEquals(type.castToWidest(PrimitiveType.SHORT),   UncertainIntegralType.getInstance(2, 4));
		assertEquals(type.castToWidest(PrimitiveType.CHAR),    UncertainIntegralType.getInstance(4, 4, UncertainIntegralType.INCLUDE_CHAR));
		assertEquals(type.castToWidest(PrimitiveType.INT),     UncertainIntegralType.getInstance(4, 4));
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
