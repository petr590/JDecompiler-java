package x590.jdecompiler.testing;

import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.UncertainReferenceType;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReferenceTypeTest {
	
	@Test
	public void testUncertainIntegralType() {
		
		assertSame(ClassType.OBJECT, ClassType.fromTypeDescriptor("Ljava/lang/Object"));
		
		ClassType child = ClassType.fromDescriptor("net/minecraft/world/level/Block");
		Type type = new UncertainReferenceType(ClassType.OBJECT, child);
		
		assertEquals(child, type.castToNarrowestNoexcept(child));
	}
}
