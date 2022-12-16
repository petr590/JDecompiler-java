package x590.javaclass.type;

import x590.javaclass.ClassInfo;
import x590.javaclass.io.ExtendedStringReader;
import x590.javaclass.util.Util;

public class ParameterType extends ReferenceType {
	
	public ParameterType(ExtendedStringReader in) {
		StringBuilder nameBuilder = new StringBuilder();
		
		for(int ch = in.read(); ch != ';' && ch != Util.EOF_CHAR; ch = in.read())
			nameBuilder.append((char)ch);
		
		this.encodedName = this.name = nameBuilder.toString();
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