package x590.jdecompiler.testing;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import x590.jdecompiler.type.reference.ArrayType;
import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.type.reference.generic.AnyGenericType;
import x590.util.Logger;

public class ClassTypeTest {
	
	@Test
	public void testParsing() {
		assertSame(ClassType.STRING, ClassType.fromDescriptor("java/lang/String"));
		assertSame(ClassType.STRING, ClassType.fromClass(String.class));
		
		assertThrows(IllegalArgumentException.class, () -> ClassType.fromClass(int.class));
		assertThrows(IllegalArgumentException.class, () -> ClassType.fromClass(Object[].class));
	}
	
	@Test
	public void testCasting() {
		
		ClassType object = ClassType.OBJECT;
		ClassType string = ClassType.STRING;
		ClassType integer = ClassType.INTEGER;
		
		ClassType block = ClassType.fromDescriptor("net/minecraft/world/level/block/Block");
		ClassType iforgeBlock = ClassType.fromDescriptor("net/minecraftforge/common/extensions/IForgeBlock");
		
		assertEquals(block, block.castToNarrowestNoexcept(iforgeBlock));
		assertNull(string.castToNarrowestNoexcept(integer));
		assertNull(string.castToNarrowestNoexcept(block));
		assertNull(block.castToNarrowestNoexcept(string));
		
		assertEquals(object, object.castToWidest(string));
	}
	
	@Test
	public void testNames() {
		ClassType mapEntryType1 = ClassType.fromDescriptor("java/util/Map$Entry<Ljava/lang/String;Ljava/lang/String;>");
		ClassType mapEntryType2 = ClassType.fromClassWithSignature(Map.Entry.class, ClassType.STRING, ClassType.STRING);
		
		testMapEntryNames(mapEntryType1);
		testMapEntryNames(mapEntryType2);
		
		testMapEntryArrayType(ArrayType.forType(mapEntryType1));
		testMapEntryArrayType(ArrayType.forType(mapEntryType2));
	}
	
	private void testMapEntryNames(ClassType classType) {
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
}
