package x590.jdecompiler.type.reference.generic;

import java.util.HashMap;
import java.util.Map;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.clazz.IClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.jdecompiler.util.StringUtil;

/** Описывает дженерик. Хранит только его имя */
public final class SignatureParameterType extends IndefiniteGenericType {
	
	private static final Map<String, SignatureParameterType> INSTANCES = new HashMap<>();
	
	private final String encodedName, name;
	
	private SignatureParameterType(String name) {
		this.name = name;
		this.encodedName = 'T' + name + ';';
	}
	
	
	public static SignatureParameterType read(ExtendedStringInputStream in) {
		StringBuilder nameBuilder = new StringBuilder();
		
		for(int ch = in.read(); ch != ';' && ch != ExtendedStringInputStream.EOF_CHAR; ch = in.read())
			nameBuilder.append((char)ch);
		
		return INSTANCES.computeIfAbsent(nameBuilder.toString(), SignatureParameterType::new);
	}
	
	
	public static SignatureParameterType of(String name) {
		return INSTANCES.computeIfAbsent(name, SignatureParameterType::new);
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
	protected boolean canCastToNarrowest(Type other) {
		return this.equals(other);
	}
	
	@Override
	public boolean equalsIgnoreSignature(Type other) {
		return this.equals(other) || other.isReferenceType();
	}
	
	@Override
	public ReferenceType toDefiniteGeneric(IClassInfo classinfo, GenericParameters<GenericDeclarationType> parameters) {
		return DefiniteGenericType.fromNullableDeclaration(
				parameters.findOrGetGenericType(name, () -> classinfo.findGenericType(name).orElse(null))
		);
	}
}
