package x590.jdecompiler.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.exception.InvalidSignatureException;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.io.ExtendedStringReader;
import x590.util.annotation.Immutable;

/** Описывает объявление дженерика. Хранит имя и супертип */
public final class GenericParameterType extends ReferenceType {
	
	private final @Immutable List<ReferenceType> types;
	
	private static String parseName(ExtendedStringReader in, StringBuilder encodedNameBuilder) {
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
	
	private static List<ReferenceType> parseTypes(ExtendedStringReader in, StringBuilder encodedNameBuilder) {
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
	
	public GenericParameterType(ExtendedStringReader in) {
		StringBuilder encodedNameBuilder = new StringBuilder();
		
		this.name = parseName(in, encodedNameBuilder);
		this.types = parseTypes(in, encodedNameBuilder);
		
		this.encodedName = encodedNameBuilder.toString();
	}
	
	@Override
	public String toString() {
		return types.size() == 1 && types.get(0).equals(ClassType.OBJECT) ? name : name + " extends " +
				types.stream().map(type -> type.toString()).collect(Collectors.joining(" & "));
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
	public String getNameForVariable() {
		throw new UnsupportedOperationException("Variable cannot have generic parameter type");
	}
	
	@Override
	protected boolean canCastTo(Type other) {
		return other.isBasicReferenceType();
	}
}
