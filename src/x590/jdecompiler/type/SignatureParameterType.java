package x590.jdecompiler.type;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.util.StringUtil;

/** Описывает дженерик. Хранит только его имя */
public final class SignatureParameterType extends ReferenceType {
	
	private final String encodedName, name;
	
	public SignatureParameterType(ExtendedStringInputStream in) {
		StringBuilder nameBuilder = new StringBuilder();
		
		for(int ch = in.read(); ch != ';' && ch != ExtendedStringInputStream.EOF_CHAR; ch = in.read())
			nameBuilder.append((char)ch);
		
		this.name = nameBuilder.toString();
		this.encodedName = 'T' + name + ';';
	}
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		out.write(name);
	}
	
	@Override
	public String toString() {
		return name;
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
	public final String getNameForVariable() {
		return StringUtil.toLowerCamelCase(name);
	}
	
	@Override
	protected boolean canCastTo(Type other) {
		return this == other;
	}
	
	@Override
	public boolean equalsIgnoreSignature(Type other) {
		return this.equals(other) || other.isReferenceType();
	}
}
