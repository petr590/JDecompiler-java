package x590.jdecompiler.testing;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import x590.jdecompiler.type.reference.ArrayType;
import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.jdecompiler.type.reference.generic.AnyGenericType;
import x590.jdecompiler.type.reference.generic.GenericDeclarationType;
import x590.jdecompiler.type.reference.generic.GenericParameters;
import x590.jdecompiler.type.reference.generic.NamedGenericType;

public class GenericParametersTest {
	
	@Test
	public void testTypesReplacing() {
		var genericDeclarationType = GenericDeclarationType.of("E", List.of(ClassType.OBJECT));
		var signatureParameterType = NamedGenericType.of("T");
		
		GenericParameters<ReferenceType> parameters = GenericParameters.of(genericDeclarationType);
		
		assertEquals(GenericParameters.of(signatureParameterType),
				parameters.replaceAllTypes(Map.of(genericDeclarationType, signatureParameterType)));
	}
	
	@Test
	public void testGenericArray() {
		var unknownClassArray = ArrayType.forType(ClassType.fromClassWithSignature(Class.class, AnyGenericType.INSTANCE));
		var classArray = ArrayType.forType(ClassType.CLASS);
		
		assertEquals(unknownClassArray, unknownClassArray.castToNarrowest(classArray));
	}
}
