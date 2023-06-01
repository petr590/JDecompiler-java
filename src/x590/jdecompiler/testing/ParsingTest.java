package x590.jdecompiler.testing;

import org.junit.Test;

import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.type.reference.ArrayType;
import x590.jdecompiler.type.reference.ClassType;
import x590.util.Logger;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class ParsingTest {
	
	@Test
	public void testMethodHeaderParsing() throws ClassNotFoundException {
		assertEquals(List.of(ClassType.STRING, ClassType.OBJECT, PrimitiveType.INT),
				Type.parseMethodArguments("(Ljava/lang/String;Ljava/lang/Object;I)"));

		assertEquals(PrimitiveType.VOID, Type.fromClass(void.class));
		assertThrows(IllegalArgumentException.class, () -> ArrayType.fromClass(Object.class));
	}
	
//	@Test
//	public void testMethodSignatureParsing() {
//		assertEquals(List.of(ClassType.STRING, ClassType.OBJECT, PrimitiveType.INT),
//				Type.parseSignature("(Ljava/lang/String;Ljava/lang/Object;I)"));
//	}
}
