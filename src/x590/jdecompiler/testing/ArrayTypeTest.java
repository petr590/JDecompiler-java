package x590.jdecompiler.testing;

import static org.junit.Assert.*;

import org.junit.Test;

import x590.jdecompiler.type.ArrayType;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.UncertainReferenceType;

public class ArrayTypeTest {
	
	@Test
	public void testArrayType() {
		assertTrue(ArrayType.forType(ClassType.STRING).isSubclassOf(ArrayType.OBJECT_ARRAY));
		assertTrue(ArrayType.OBJECT_ARRAY.isSubclassOf(ArrayType.ANY_ARRAY));
		assertTrue(new UncertainReferenceType(ArrayType.OBJECT_ARRAY).isSubtypeOf(ArrayType.ANY_ARRAY));
	}
}
