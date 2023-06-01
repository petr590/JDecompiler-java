package x590.jdecompiler.testing;

import x590.jdecompiler.type.CastingKind;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.Types;
import x590.jdecompiler.type.UncertainReferenceType;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.type.reference.ArrayType;
import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.jdecompiler.type.reference.generic.AnyGenericType;
import x590.jdecompiler.type.reference.generic.DefiniteGenericType;

import org.junit.Test;
import x590.util.Logger;

import static org.junit.Assert.*;

import java.util.List;

public class UncertainReferenceTypeTest {
	
	@Test
	public void testWithReferenceTypes() {
		
		ReferenceType parent = ClassType.OBJECT;
		ReferenceType child = ClassType.fromDescriptor("net/minecraft/world/level/block/Block");
		ReferenceType other = ClassType.fromDescriptor("net/minecraft/world/item/Item");
		
		Type type = UncertainReferenceType.getInstance(parent, child);
		
		assertEquals(child, type.castToNarrowestNoexcept(child));
		assertEquals(type, type.castToNarrowestNoexcept(parent));
		
		assertEquals(type, type.castToWidestNoexcept(child));
		assertEquals(parent, type.castToWidestNoexcept(parent));
		
		assertNull(type.castToNarrowestNoexcept(other));
	}
	
	@Test
	public void testWithUncertainReferenceTypes() {
		
		ReferenceType object = ClassType.OBJECT;
		ReferenceType number = ClassType.fromClass(Number.class);
		ReferenceType integer = ClassType.INTEGER;
		ReferenceType string = ClassType.STRING;
		
		Type objectInteger = UncertainReferenceType.getInstance(object, integer);
		Type objectNumber = UncertainReferenceType.getInstance(object, number);
		Type numberInteger = UncertainReferenceType.getInstance(number, integer);
		
		assertEquals(numberInteger, objectInteger.castToNarrowestNoexcept(number));
		assertEquals(objectInteger, objectInteger.castToNarrowestNoexcept(objectNumber));
		assertEquals(objectNumber, objectNumber.castToNarrowestNoexcept(objectInteger));
		
		assertEquals(objectNumber, objectInteger.castToWidestNoexcept(number));
		assertEquals(objectNumber, objectInteger.castToWidestNoexcept(objectNumber));
		assertEquals(objectInteger, objectInteger.castToWidestNoexcept(integer));
		
		assertNull(objectNumber.castToNarrowestNoexcept(integer));
		assertNull(objectNumber.castToNarrowestNoexcept(UncertainReferenceType.getInstance(integer)));
		assertNull(objectNumber.castToNarrowestNoexcept(string));
//		assertNull(objectNumber.castToNarrowestNoexcept(UncertainReferenceType.getInstance(string)));
	}
	
	@Test
	public void testWithGenericTypes() {
		
		ReferenceType parent = ClassType.fromClass(Number.class);
		ReferenceType child = DefiniteGenericType.of("T", parent, List.of());
		ReferenceType other = ClassType.INTEGER;
		
		Type type = UncertainReferenceType.getInstance(parent, child);
		
		assertEquals(child, type.castToNarrowestNoexcept(child));
		assertEquals(type, type.castToNarrowestNoexcept(parent));
		
		assertEquals(type, type.castToWidestNoexcept(child));
		assertEquals(parent, type.castToWidestNoexcept(parent));
		
		assertNull(type.castToNarrowestNoexcept(other));
	}
	
	@Test
	public void testWithParametrizedTypes() {
		
		ReferenceType anyList = ClassType.fromClassWithSignature(List.class, AnyGenericType.INSTANCE);
		ReferenceType strList = ClassType.fromClassWithSignature(List.class, ClassType.STRING);
		
		Type type = UncertainReferenceType.getInstance(ClassType.OBJECT, strList);
		
		assertEquals(strList, type.castToNarrowestNoexcept(anyList));
	}
	
	@Test
	public void testWithArrayTypes() {

		Type array1 = UncertainReferenceType.getInstance(ClassType.OBJECT, ArrayType.STRING_ARRAY);
		Type array2 = UncertainReferenceType.getInstance(ArrayType.OBJECT_ARRAY, ArrayType.STRING_ARRAY);

		assertEquals(array2, array1.castToNarrowestNoexcept(array2));
		
		Type type1 = UncertainReferenceType.getInstance(ClassType.OBJECT, ArrayType.INT_ARRAY);
		Type type2 = UncertainReferenceType.getInstance(ArrayType.ANY_ARRAY, ArrayType.INT_ARRAY);
		
		assertTrue(PrimitiveType.INT.isDefinitelySubtypeOf(Types.ANY_TYPE));
		
		assertNotNull(type2);
		assertEquals(type2, type1.castToNarrowestNoexcept(ArrayType.ANY_ARRAY));
		assertEquals(type2, type1.castToNarrowest(ArrayType.ANY_ARRAY));
		
		ReferenceType byteArray = ArrayType.BYTE_ARRAY;
		Type uncertainByteArray = UncertainReferenceType.getInstance(byteArray);
		Type uncertainByteOrBooleanArray = UncertainReferenceType.getInstance(ArrayType.BYTE_OR_BOOLEAN_ARRAY);
		
		assertEquals(byteArray, byteArray.castToNarrowestNoexcept(uncertainByteOrBooleanArray));
		assertEquals(uncertainByteArray, uncertainByteOrBooleanArray.castToNarrowestNoexcept(byteArray));
		assertEquals(UncertainReferenceType.getInstance(ArrayType.BYTE_OR_BOOLEAN_ARRAY, ArrayType.BYTE_ARRAY),
				uncertainByteOrBooleanArray.castToWidestNoexcept(byteArray));
	}
}
