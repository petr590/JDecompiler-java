package x590.jdecompiler.testing;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.junit.Test;

import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.jdecompiler.type.reference.generic.AnyGenericType;
import x590.util.Logger;

public class GenericsTest {
	
	@Test
	public void testRecursive() {
		var parameter = Enum.class.getTypeParameters()[0];
		
		assertEquals(parameter, ((ParameterizedType)parameter.getBounds()[0]).getActualTypeArguments()[0]);
		
		Logger.debug(ReferenceType.fromReflectType(parameter));
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
