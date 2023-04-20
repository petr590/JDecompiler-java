package x590.jdecompiler.testing;

import x590.jdecompiler.exception.IncopatibleTypesException;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.GeneralCastingKind;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.UncertainIntegralType;

import org.junit.Test;

import static org.junit.Assert.*;
import static x590.jdecompiler.type.PrimitiveType.*;

public class PrimitiveTypeTest {
	
	private static final UncertainIntegralType
			UNCERTAIN_BYTE = UncertainIntegralType.getInstance(1, 1),
			BYTE_SHORT     = UncertainIntegralType.getInstance(1, 2),
			UNCERTAIN_INT  = UncertainIntegralType.getInstance(4, 4);
	
	@Test
	public void testUncertainIntegralType() {
		Type type = BYTE_SHORT_INT_CHAR_BOOLEAN;
		
		assertEquals(BOOLEAN,             type.castToNarrowest(BOOLEAN));
		assertEquals(UNCERTAIN_BYTE,      type.castToNarrowest(BYTE));
		assertEquals(BYTE_SHORT,          type.castToNarrowest(SHORT));
		assertEquals(CHAR,                type.castToNarrowest(CHAR));
		assertEquals(BYTE_SHORT_INT_CHAR, type.castToNarrowest(INT));
		
		assertEquals(BOOLEAN,        type.castToWidest(BOOLEAN));
		assertEquals(BYTE_SHORT_INT, type.castToWidest(BYTE));
		assertEquals(SHORT_INT,      type.castToWidest(SHORT));
		assertEquals(INT_CHAR,       type.castToWidest(CHAR));
		assertEquals(UNCERTAIN_INT,  type.castToWidest(INT));
		
		assertEquals(SHORT, SHORT.castToWidest(BYTE));
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
		
		assertEquals(BYTE, BYTE.castToNarrowest(SHORT));
		assertEquals(BYTE, BYTE.castToNarrowest(INT));
		
		assertThrows(IncopatibleTypesException.class, () -> SHORT.castToNarrowest(BYTE));
		assertEquals(SHORT, SHORT.castToNarrowest(INT));
		
		assertThrows(IncopatibleTypesException.class, () -> INT.castToNarrowest(BYTE));
		assertThrows(IncopatibleTypesException.class, () -> INT.castToNarrowest(SHORT));
		
		assertThrows(IncopatibleTypesException.class, () -> CHAR.castToNarrowest(BYTE));
		assertThrows(IncopatibleTypesException.class, () -> CHAR.castToNarrowest(SHORT));
		assertEquals(CHAR, CHAR.castToNarrowest(INT));
	}
	
	@Test
	public void testPrimitiveImplicitCast() {
		testImplicitCastToGeneral(BYTE, SHORT, INT);
		testImplicitCastToGeneral(BYTE, CHAR, INT);
		testImplicitCastToGeneral(BYTE, INT);
		testImplicitCastToGeneral(BYTE, LONG);
		testImplicitCastToGeneral(BYTE, FLOAT);
		testImplicitCastToGeneral(BYTE, DOUBLE);
		
		testImplicitCastToGeneral(SHORT, CHAR, INT);
		testImplicitCastToGeneral(SHORT, INT);
		testImplicitCastToGeneral(SHORT, LONG);
		testImplicitCastToGeneral(SHORT, FLOAT);
		testImplicitCastToGeneral(SHORT, DOUBLE);
		
		testImplicitCastToGeneral(CHAR, INT);
		testImplicitCastToGeneral(CHAR, LONG);
		testImplicitCastToGeneral(CHAR, FLOAT);
		testImplicitCastToGeneral(CHAR, DOUBLE);
		
		testImplicitCastToGeneral(INT, LONG);
		testImplicitCastToGeneral(INT, FLOAT);
		testImplicitCastToGeneral(INT, DOUBLE);
		
		testImplicitCastToGeneral(LONG, FLOAT);
		testImplicitCastToGeneral(LONG, DOUBLE);
		
		testImplicitCastToGeneral(FLOAT, DOUBLE);
	}
	
	private void testImplicitCastToGeneral(Type narrowest, Type widest) {
		testImplicitCastToGeneral(narrowest, widest, widest);
	}
	
	private void testImplicitCastToGeneral(Type narrowest, Type widest, Type expected) {
		assertEquals(expected, narrowest.implicitCastToGeneralNoexcept(widest, GeneralCastingKind.BINARY_OPERATOR));
		assertEquals(expected, widest.implicitCastToGeneralNoexcept(narrowest, GeneralCastingKind.BINARY_OPERATOR));
	}
	
	@Test
	public void testWrappedImplicitCast() {
		testWrappedImplicitCastToGeneral(BYTE, SHORT, INT);
		testWrappedImplicitCastToGeneral(BYTE, CHAR, INT);
		testWrappedImplicitCastToGeneral(BYTE, INT);
		testWrappedImplicitCastToGeneral(BYTE, LONG);
		testWrappedImplicitCastToGeneral(BYTE, FLOAT);
		testWrappedImplicitCastToGeneral(BYTE, DOUBLE);
		
		testWrappedImplicitCastToGeneral(SHORT, CHAR, INT);
		testWrappedImplicitCastToGeneral(SHORT, INT);
		testWrappedImplicitCastToGeneral(SHORT, LONG);
		testWrappedImplicitCastToGeneral(SHORT, FLOAT);
		testWrappedImplicitCastToGeneral(SHORT, DOUBLE);
		
		testWrappedImplicitCastToGeneral(CHAR, INT);
		testWrappedImplicitCastToGeneral(CHAR, LONG);
		testWrappedImplicitCastToGeneral(CHAR, FLOAT);
		testWrappedImplicitCastToGeneral(CHAR, DOUBLE);
		
		testWrappedImplicitCastToGeneral(INT, LONG);
		testWrappedImplicitCastToGeneral(INT, FLOAT);
		testWrappedImplicitCastToGeneral(INT, DOUBLE);
		
		testWrappedImplicitCastToGeneral(LONG, FLOAT);
		testWrappedImplicitCastToGeneral(LONG, DOUBLE);
		
		testWrappedImplicitCastToGeneral(FLOAT, DOUBLE);
		
		assertEquals(FLOAT, ClassType.FLOAT.implicitCastToGeneralNoexcept(INT, GeneralCastingKind.BINARY_OPERATOR));
		
		assertEquals(null, ClassType.BYTE.implicitCastToGeneralNoexcept(ClassType.SHORT, GeneralCastingKind.EQUALS_COMPARASION));
		
		assertEquals(BOOLEAN, ClassType.BOOLEAN.implicitCastToGeneralNoexcept(ClassType.BOOLEAN, GeneralCastingKind.BINARY_OPERATOR));
		assertEquals(ClassType.BOOLEAN, ClassType.BOOLEAN.implicitCastToGeneralNoexcept(ClassType.BOOLEAN, GeneralCastingKind.TERNARY_OPERATOR));
	}
	
	private void testWrappedImplicitCastToGeneral(PrimitiveType narrowest, PrimitiveType widest) {
		testImplicitCastToGeneral(narrowest, widest, widest);
	}
	
	private void testWrappedImplicitCastToGeneral(PrimitiveType narrowest, PrimitiveType widest, PrimitiveType expected) {
		assertEquals(expected, narrowest.implicitCastToGeneralNoexcept(widest, GeneralCastingKind.BINARY_OPERATOR));
		assertEquals(expected, widest.implicitCastToGeneralNoexcept(narrowest, GeneralCastingKind.BINARY_OPERATOR));
		assertEquals(expected, narrowest.getWrapperType().implicitCastToGeneralNoexcept(widest, GeneralCastingKind.BINARY_OPERATOR));
		assertEquals(expected, widest.getWrapperType().implicitCastToGeneralNoexcept(narrowest, GeneralCastingKind.BINARY_OPERATOR));
	}
}
