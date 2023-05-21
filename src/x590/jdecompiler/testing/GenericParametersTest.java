package x590.jdecompiler.testing;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.jdecompiler.type.reference.generic.GenericDeclarationType;
import x590.jdecompiler.type.reference.generic.GenericParameters;
import x590.jdecompiler.type.reference.generic.SignatureParameterType;

public class GenericParametersTest {
	
	@Test
	public void testTypesReplacing() {
		var genericDeclarationType = GenericDeclarationType.of("E", List.of(ClassType.OBJECT));
		var signatureParameterType = SignatureParameterType.of("T");
		
		GenericParameters<ReferenceType> parameters = GenericParameters.of(genericDeclarationType);
		
		assertEquals(GenericParameters.of(signatureParameterType), GenericParameters.replaceAllTypes(parameters,
				Map.of(genericDeclarationType, signatureParameterType)));
	}
}
