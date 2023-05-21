package x590.jdecompiler.type.reference.generic;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.jdecompiler.util.StringUtil;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public class DefiniteGenericType extends GenericType {
	
	private final String encodedName, name;
	private final ReferenceType superType;
	private final @Immutable List<? extends ReferenceType> interfaces;
	
	private DefiniteGenericType(String encodedName, String name, ReferenceType superType, @Immutable List<? extends ReferenceType> interfaces) {
		this.encodedName = encodedName;
		this.name        = name;
		this.superType   = superType;
		this.interfaces  = interfaces;
	}
	
	private DefiniteGenericType(GenericDeclarationType genericDeclaration) {
		this.encodedName = genericDeclaration.getSimpleEncodedName();
		this.name        = genericDeclaration.getName();
		this.superType   = genericDeclaration.getSuperType();
		this.interfaces  = genericDeclaration.getInterfaces();
	}
	
	/* package-visible */ static ReferenceType of(String encodedName, String name, ReferenceType superType, @Immutable List<? extends ReferenceType> interfaces) {
		return new DefiniteGenericType(encodedName, name, superType, interfaces);
	}
	
	public static ReferenceType of(String name, ReferenceType superType, @Immutable List<? extends ReferenceType> interfaces) {
		return new DefiniteGenericType('T' + name + ';', name, superType, interfaces);
	}
	
	public static ReferenceType fromDeclaration(GenericDeclarationType genericDeclaration) {
		return new DefiniteGenericType(genericDeclaration);
	}
	
	public static @Nullable ReferenceType fromNullableDeclaration(@Nullable GenericDeclarationType genericDeclaration) {
		return genericDeclaration == null ? null : new DefiniteGenericType(genericDeclaration);
	}
	
	
	@Override
	public @Nullable ReferenceType getSuperType() {
		return superType;
	}
	
	@Override
	public @Nullable @Immutable List<? extends ReferenceType> getInterfaces() {
		return interfaces;
	}
	
	
	@Override
	public ReferenceType replaceAllTypes(@Immutable Map<GenericDeclarationType, ReferenceType> replaceTable) {
		return replaceTable.entrySet().stream()
				.filter(entry -> entry.getKey().getName().equals(name))
				.findAny().map(Map.Entry::getValue).orElse(this);
	}
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo param) {
		out.write(name);
	}
	
	@Override
	public String toString() {
		return name + " extends " + superType +
				(interfaces.isEmpty() ? "" : " implements " +
						interfaces.stream().map(Type::toString).collect(Collectors.joining(", ")));
	}
	
	@Override
	public String getEncodedName() {
		return encodedName;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getNameForVariable() {
		return StringUtil.toLowerCamelCase(name);
	}
}
