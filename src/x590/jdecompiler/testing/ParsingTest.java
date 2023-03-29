package x590.jdecompiler.testing;

import org.junit.Test;

import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;

import static org.junit.Assert.*;

import java.util.List;

public class ParsingTest {
	
	@Test
	public void testMethodHeaderParsing() {
		assertEquals(List.of(ClassType.STRING, ClassType.OBJECT, PrimitiveType.INT),
				Type.parseMethodArguments("(Ljava/lang/String;Ljava/lang/Object;I)"));
	}
	
//	@Test
//	public void testMethodSignatureParsing() {
//		assertEquals(List.of(ClassType.STRING, ClassType.OBJECT, PrimitiveType.INT),
//				Type.parseSignature("(Ljava/lang/String;Ljava/lang/Object;I)"));
//	}
}
