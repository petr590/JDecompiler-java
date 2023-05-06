package x590.jdecompiler.testing;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.type.reference.ClassType;
import x590.util.CollectionUtil;

public class EqualsIgnoreSignatureTest {
	
	@Test
	public void testEqualsIgnoreSignature() {
		assertTrue(CollectionUtil.collectionsEquals(
				List.of(
						ClassType.fromDescriptor("Ljava/util/Map<Ljava/lang/Class<+Ljava/lang/annotation/Annotation;>;Ljava/lang/annotation/Annotation;>;"),
						ClassType.fromDescriptor("Ljava/util/Map<Ljava/lang/Class<+Ljava/lang/annotation/Annotation;>;Ljava/lang/annotation/Annotation;>;"),
						PrimitiveType.INT
				),
				List.of(
						ClassType.fromDescriptor("Ljava/util/Map;"),
						ClassType.fromDescriptor("Ljava/util/Map;"),
						PrimitiveType.INT
				),
				Type::equalsIgnoreSignature
		));
	}
}
