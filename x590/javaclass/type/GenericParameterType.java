package x590.javaclass.type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import x590.javaclass.ClassInfo;
import x590.javaclass.exception.InvalidSignatureException;
import x590.javaclass.io.ExtendedStringReader;

/** Описывает объявление дженерика. Хранит имя и супертип */
public class GenericParameterType extends ReferenceType {
	
	public final List<ReferenceType> types;
	
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
		if(in.get() == ':') {
			in.incPos();
			encodedNameBuilder.append(':');
		}
		
		List<ReferenceType> types = new ArrayList<>();
		types.add(Type.parseSignatureParameter(in));
		
		while(in.get() == ':') {
			ReferenceType type = Type.parseSignatureParameter(in.next());
			types.add(type);
			encodedNameBuilder.append(':').append(type.getEncodedName());
		}
		
		return types;
	}
	
	public GenericParameterType(ExtendedStringReader in) {
		StringBuilder encodedNameBuilder = new StringBuilder();
		
		this.name = parseName(in, encodedNameBuilder);
		this.types = parseTypes(in, encodedNameBuilder);
		
		this.encodedName = encodedNameBuilder.toString();
	}
	
	@Override
	public String toString() {
		return types.size() == 1 && types.get(0) == ClassType.OBJECT ? name : name + " extends " +
				types.stream().map(type -> type.toString()).collect(Collectors.joining(" & "));
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return types.size() == 1 && types.get(0) == ClassType.OBJECT ? name : name + " extends " +
				types.stream().map(type -> type.toString(classinfo)).collect(Collectors.joining(" & "));
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