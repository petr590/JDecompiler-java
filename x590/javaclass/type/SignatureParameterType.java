package x590.javaclass.type;

import x590.javaclass.ClassInfo;
import x590.javaclass.io.ExtendedStringReader;
import x590.javaclass.util.Util;

/** Описывает дженерик. Хранит только его имя */
public class SignatureParameterType extends ReferenceType {
	
	public SignatureParameterType(ExtendedStringReader in) {
		StringBuilder nameBuilder = new StringBuilder();
		
		for(int ch = in.read(); ch != ';' && ch != ExtendedStringReader.EOF_CHAR; ch = in.read())
			nameBuilder.append((char)ch);
		
		this.name = nameBuilder.toString();
		this.encodedName = "T" + name + ";";
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return name;
	}
	
	@Override
	public final String getNameForVariable() {
		return Util.toLowerCamelCase(name);
	}
	
	@Override
	protected boolean canCastTo(Type other) {
		return this == other;
	}
}