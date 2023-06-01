package x590.jdecompiler.type.reference.generic;

import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.jdecompiler.util.StringUtil;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public class DefiniteGenericType extends GenericType {
	
	private static final Map<TypeVariable<?>, DefiniteGenericType> TYPES = new HashMap<>();
	
	private final String encodedName, name;
	private final ReferenceType superType;
	private final @Immutable List<? extends ReferenceType> interfaces;
	
	
	private DefiniteGenericType(String encodedName, String name, ReferenceType superType, @Immutable List<? extends ReferenceType> interfaces) {
		
		this.encodedName = encodedName;
		this.name        = name;
		this.superType   = superType;
		this.interfaces  = interfaces;
	}
	
	private DefiniteGenericType(TypeVariable<?> typeVariable) {
		java.lang.reflect.Type[] bounds = typeVariable.getBounds();
		
		// Кэширование должно происходить ДО инициализации superType и interfaces.
		// Это необходимо для избежания бесконечной рекурсии для таких типов, как E extends Enum<E>
		TYPES.put(typeVariable, this);
		
		this.name = typeVariable.getName();
		this.encodedName = 'T' + name + ';';
		this.superType = ReferenceType.fromReflectType(bounds[0]);
		this.interfaces = Arrays.stream(bounds, 1, bounds.length).map(ReferenceType::fromReflectType).toList();
	}


	public static DefiniteGenericType of(String name, ReferenceType superType, @Immutable List<? extends ReferenceType> interfaces) {
		return new DefiniteGenericType('T' + name + ';', name, superType, interfaces);
	}
	
	
	/** @param superTypeAndInterfaces - супертип и интерфейсы в одном списке. Не должен быть пустым */
	public static DefiniteGenericType of(String name, @Immutable List<? extends ReferenceType> superTypeAndInterfaces) {
		return of(name, name, superTypeAndInterfaces.get(0), superTypeAndInterfaces.subList(1, superTypeAndInterfaces.size()));
	}
	
	
	/* package-visible */ static DefiniteGenericType of(String encodedName, String name, ReferenceType superType, @Immutable List<? extends ReferenceType> interfaces) {
		return new DefiniteGenericType(encodedName, name, superType, interfaces);
	}
	
	
	public static DefiniteGenericType fromTypeVariable(TypeVariable<?> typeVariable) {
		
		DefiniteGenericType type = TYPES.get(typeVariable);
		
		if(type != null)
			return type;
		
		return new DefiniteGenericType(typeVariable);
	}
	
	public static DefiniteGenericType fromDeclaration(GenericDeclarationType genericDeclaration) {
		return of(
				genericDeclaration.getSimpleEncodedName(),
				genericDeclaration.getName(),
				genericDeclaration.getSuperType(),
				genericDeclaration.getInterfaces()
		);
	}
	
	public static @Nullable DefiniteGenericType fromNullableDeclaration(@Nullable GenericDeclarationType genericDeclaration) {
		return genericDeclaration == null ? null : fromDeclaration(genericDeclaration);
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
		return name /* + " extends " + superType + (interfaces.isEmpty() ? "" : " implements " +
				interfaces.stream().map(Type::toString).collect(Collectors.joining(", "))) */;
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
