package x590.jdecompiler.testing;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import x590.jdecompiler.type.reference.ArrayType;
import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.jdecompiler.type.reference.generic.AnyGenericType;
import x590.util.Logger;

public class ReferenceTypeTest {
	
	@Test
	public void test() {
		
		ClassType string = ClassType.STRING;
		ClassType integer = ClassType.INTEGER;
		
		ClassType block = ClassType.fromDescriptor("net/minecraft/world/level/block/Block");
		ClassType iforgeBlock = ClassType.fromDescriptor("net/minecraftforge/common/extensions/IForgeBlock");
		
		assertEquals(block, block.castToNarrowestNoexcept(iforgeBlock));
		assertNull(string.castToNarrowestNoexcept(integer));
		assertNull(string.castToNarrowestNoexcept(block));
		assertNull(block.castToNarrowestNoexcept(string));
	}
	
	@Test
	public void testClassTypes() {
		ClassType mapEntryType1 = ClassType.fromDescriptor("java/util/Map$Entry<Ljava/lang/String;Ljava/lang/String;>");
		ClassType mapEntryType2 = ClassType.fromClassWithSignature(Map.Entry.class, ClassType.STRING, ClassType.STRING);
		
		testMapEntryClassType(mapEntryType1);
		testMapEntryClassType(mapEntryType2);
		
		testMapEntryArrayType(ArrayType.forType(mapEntryType1));
		testMapEntryArrayType(ArrayType.forType(mapEntryType2));
	}
	
	private void testMapEntryClassType(ClassType classType) {
		assertEquals("Ljava/util/Map$Entry<Ljava/lang/String;Ljava/lang/String;>;", classType.getEncodedName());
		assertEquals("java/util/Map$Entry<Ljava/lang/String;Ljava/lang/String;>", classType.getClassEncodedName());
		assertEquals("java.util.Map.Entry", classType.getName());
		assertEquals("java.util.Map$Entry", classType.getBinaryName());
		assertEquals("Entry", classType.getSimpleName());
		assertEquals("Map.Entry", classType.getFullSimpleName());
		assertEquals("java.util", classType.getPackageName());
		assertSame(Map.Entry.class, classType.getClassInstance());
	}
	
	private void testMapEntryArrayType(ArrayType arrayType) {
		assertEquals("[Ljava/util/Map$Entry<Ljava/lang/String;Ljava/lang/String;>;", arrayType.getEncodedName());
		assertEquals("[java/util/Map$Entry<Ljava/lang/String;Ljava/lang/String;>", arrayType.getClassEncodedName());
		assertEquals("java.util.Map.Entry[]", arrayType.getName());
		assertEquals("[Ljava.util.Map$Entry;", arrayType.getBinaryName());
		assertSame(Map.Entry[].class, arrayType.getClassInstance());
	}
	
	@Test
	public void testClassTypesEquals() {
		
		ClassType list = ClassType.fromClass(List.class);
		ClassType anyList = ClassType.fromClassWithSignature(List.class, AnyGenericType.INSTANCE);
		ClassType numList = ClassType.fromClassWithSignature(List.class, ClassType.fromClass(Number.class));
		ClassType strList = ClassType.fromClassWithSignature(List.class, ClassType.fromClass(String.class));
		
		Logger.debug(strList.getEncodedName(), numList.getEncodedName());
		
		assertTrue(list.equalsIgnoreSignature(anyList));
		assertTrue(list.equalsIgnoreSignature(numList));
		assertTrue(strList.equalsIgnoreSignature(numList));
		
		assertFalse(list.equals(anyList));
		assertFalse(list.equals(numList));
		assertFalse(strList.equals(numList));
	}
	
	@Test
	public void testParametrizedClassTypes() {

		ReferenceType list = ClassType.fromClass(List.class);
		ReferenceType anyList = ClassType.fromClassWithSignature(List.class, AnyGenericType.INSTANCE);
		ReferenceType numList = ClassType.fromClassWithSignature(List.class, ClassType.fromClass(Number.class));
		ReferenceType strList = ClassType.fromClassWithSignature(List.class, ClassType.STRING);
		
		ReferenceType clazz = ClassType.fromClass(Class.class);
		ReferenceType anyClass = ClassType.fromClassWithSignature(Class.class, AnyGenericType.INSTANCE);
		
		assertEquals(anyList.getSuperType(), numList.getSuperType());
		assertEquals(anyList.getInterfaces(), numList.getInterfaces());
		
		assertEquals(list, list.castToNarrowestNoexcept(anyList));
		assertEquals(clazz, clazz.castToNarrowestNoexcept(anyClass));
		assertEquals(numList, numList.castToNarrowestNoexcept(anyList));
		assertEquals(numList, numList.castToNarrowestNoexcept(strList));
	}
}
