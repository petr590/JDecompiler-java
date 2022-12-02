package x590.javaclass.type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import x590.javaclass.ClassInfo;
import x590.javaclass.Stringified;
import x590.javaclass.exception.InvalidSignatureException;
import x590.javaclass.io.ExtendedStringReader;

public class GenericParameter implements Stringified {
	
	public final String name;
	public final List<ReferenceType> types;
	
	private static String parseName(ExtendedStringReader in) {
		StringBuilder nameBuilder = new StringBuilder();
		
		for(int ch = in.read(); ch != ':'; ch = in.read())
			nameBuilder.append(ch);
		
		in.decPos();
		
		if(nameBuilder.isEmpty())
			throw new InvalidSignatureException(in);
		
		return nameBuilder.toString();
	}
	
	private static List<ReferenceType> parseTypes(ExtendedStringReader in) {
		if(in.get() == ':')
			in.incPos();
		
		List<ReferenceType> types = new ArrayList<>();
		types.add(Type.parseParameter(in));
		
		while(in.get() == ':') {
			types.add(Type.parseParameter(in.next()));
		}
		
		return types;
	}
	
	public GenericParameter(ExtendedStringReader in) {
		name = parseName(in);
		types = parseTypes(in);
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return types.size() == 1 && types.get(0) == ClassType.OBJECT ? name : name + " extends " +
				types.stream().map(type -> type.toString(classinfo)).collect(Collectors.joining(" & "));
	}
}