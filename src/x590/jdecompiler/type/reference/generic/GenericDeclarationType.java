package x590.jdecompiler.type.reference.generic;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.clazz.IClassInfo;
import x590.jdecompiler.exception.InvalidSignatureException;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

/** Описывает объявление дженерика. Хранит имя и супертип */
public final class GenericDeclarationType extends ReferenceType {
	
	private final String encodedName, simpleEncodedName, name;
	private final @Immutable List<ReferenceType> types;
	private final @Nullable ReferenceType superType;
	private final @Immutable List<ReferenceType> interfaces;
	
	private static String parseName(ExtendedStringInputStream in, StringBuilder encodedNameBuilder) {
		StringBuilder nameBuilder = new StringBuilder();
		
		for(int ch = in.read(); ch != ':'; ch = in.read()) {
			nameBuilder.append((char)ch);
			encodedNameBuilder.append((char)ch);
		}
		
		in.decPos();
		
		if(nameBuilder.isEmpty())
			throw new InvalidSignatureException(in);
		
		return nameBuilder.toString();
	}
	
	private static List<ReferenceType> parseTypes(ExtendedStringInputStream in, StringBuilder encodedNameBuilder) {
		List<ReferenceType> types = new ArrayList<>();
		
		do {
			if(in.get() != ':')
				throw new InvalidSignatureException(in, in.distanceToMark());
			
			do {
				in.incPos();
			} while(in.get() == ':');
			
			ReferenceType type = Type.parseSignatureParameter(in);
			types.add(type);
			encodedNameBuilder.append(':').append(type.getEncodedName());
			
		} while(in.get() == ':');
		
		return Collections.unmodifiableList(types);
	}
	
	
	private @Nullable ReferenceType superTypeFromTypes(@Immutable List<ReferenceType> types) {
		return types.isEmpty() ? null : types.get(0);
	}
	
	private List<ReferenceType> interfacesFromTypes(@Immutable List<ReferenceType> types) {
		return types.isEmpty() ? Collections.emptyList() : types.subList(1, types.size());
	}
	
	
	private GenericDeclarationType(ExtendedStringInputStream in) {
		StringBuilder encodedNameBuilder = new StringBuilder();
		
		this.name = parseName(in, encodedNameBuilder);
		this.types = parseTypes(in, encodedNameBuilder);
		this.superType = superTypeFromTypes(types);
		this.interfaces = interfacesFromTypes(types);
		
		this.simpleEncodedName = 'T' + name + ';';
		this.encodedName = encodedNameBuilder.toString();
	}
	
	private GenericDeclarationType(String name, @Immutable List<ReferenceType> types) {
		this.name = name;
		this.types = types;
		this.superType = superTypeFromTypes(types);
		this.interfaces = interfacesFromTypes(types);

		this.simpleEncodedName = 'T' + name + ';';
		this.encodedName = name + ':' + types.stream().map(Type::getEncodedName).collect(Collectors.joining("::"));
	}
	
	
	public static GenericDeclarationType read(ExtendedStringInputStream in) {
		return new GenericDeclarationType(in);
	}
	
	public static GenericDeclarationType of(String name, @Immutable List<ReferenceType> types) {
		return new GenericDeclarationType(name, types);
	}
	
	public static GenericDeclarationType fromTypeVariable(TypeVariable<?> reflectType, IClassInfo classinfo) {
		return new GenericDeclarationType(reflectType.getTypeName(),
				Arrays.stream(reflectType.getBounds())
					.map(bound -> ReferenceType.fromReflectType(reflectType, classinfo)).toList());
	}
	
	
	@Override
	public @Nullable ReferenceType getSuperType() {
		return superType;
	}
	
	@Override
	public @Immutable List<? extends ReferenceType> getInterfaces() {
		return interfaces;
	}
	
	
	@Override
	public String toString() {
		return "decl(" + (types.size() == 1 && types.get(0).equals(ClassType.OBJECT) ? name : name + " extends " +
				types.stream().map(Type::toString).collect(Collectors.joining(" & "))) + ')';
	}
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		out.write(name);
		
		if(types.size() != 1 || !types.get(0).equals(ClassType.OBJECT)) {
			out.print(" extends ").printAllObjects(types, classinfo, " & ");
		}
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImportsFor(types);
	}
	
	@Override
	public String getEncodedName() {
		return encodedName;
	}
	
	public String getSimpleEncodedName() {
		return simpleEncodedName;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	
	@Override
	public String getNameForVariable() {
		throw new UnsupportedOperationException("Variable cannot have generic declaration type");
	}
	
	@Override
	protected boolean canCastToNarrowestImpl(Type other) {
		return other.isReferenceType();
	}
	
	@Override
	protected boolean canCastToWidestImpl(Type other) {
		return other.isReferenceType();
	}
	
	
	@Override
	public ReferenceType replaceUndefiniteGenericsToDefinite(IClassInfo classinfo, GenericParameters<GenericDeclarationType> parameters) {
		return DefiniteGenericType.fromDeclaration(this);
	}
	
	@Override
	public ReferenceType replaceAllTypes(@Immutable Map<GenericDeclarationType, ReferenceType> replaceTable) {
		return replaceTable.getOrDefault(this, this);
	}
	
	
//	@Override
//	public @Nullable GenericParameters<? extends ReferenceType> narrowGenericParameters(
//			ReferenceType prevType, GenericParameters<? extends ReferenceType> parameters) {
//		
//		// TODO
//	}
}
