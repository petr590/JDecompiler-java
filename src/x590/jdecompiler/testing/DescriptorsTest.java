package x590.jdecompiler.testing;

import static org.junit.Assert.*;
import static x590.jdecompiler.type.primitive.PrimitiveType.VOID;
import static x590.jdecompiler.type.reference.ClassType.OBJECT;

import org.junit.Test;

import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.type.reference.ClassType;

public class DescriptorsTest {
	
	@Test
	public void testMethodDescriptorsEquals() {
		var descriptor1 = MethodDescriptor.of(VOID, OBJECT, "method", ClassType.fromDescriptor("java/util/Map;"));
		var descriptor2 = MethodDescriptor.of(VOID, OBJECT, "method", ClassType.fromDescriptor("java/util/Map;"));
		var descriptor3 = MethodDescriptor.of(VOID, OBJECT, "method", ClassType.fromDescriptor("java/util/Map<Ljava/lang/String;Ljava/lang/String;>;"));
		
		assertTrue(descriptor1.equals(descriptor2));
		assertFalse(descriptor1.equals(descriptor3));
	}
}
