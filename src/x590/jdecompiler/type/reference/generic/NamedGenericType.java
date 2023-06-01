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
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

/** Описывает дженерик. Хранит только его имя */
public final class NamedGenericType extends IndefiniteGenericType {
	
	private static final Map<String, NamedGenericType> INSTANCES = new HashMap<>();
	
	private final String encodedName, name;
	
	private NamedGenericType(String name) {
		if(!name.matches("\\w+")) {
			throw new IllegalArgumentException("name: " + name);
		}
		
		this.name = name;
		this.encodedName = 'T' + name + ';';
	}
	
	
	public static NamedGenericType read(ExtendedStringInputStream in) {
		StringBuilder nameBuilder = new StringBuilder();
		
		for(int ch = in.read(); ch != ';' && ch != ExtendedStringInputStream.EOF_CHAR; ch = in.read())
			nameBuilder.append((char)ch);
		
		return INSTANCES.computeIfAbsent(nameBuilder.toString(), NamedGenericType::new);
	}
	
	
	public static NamedGenericType of(String name) {
		return INSTANCES.computeIfAbsent(name, NamedGenericType::new);
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
	protected boolean canCastToNarrowestImpl(Type other) {
		return this.equals(other);
	}
	
	@Override
	public boolean equalsIgnoreSignature(Type other) {
		return this.equals(other) || other.isReferenceType();
	}
	
	@Override
	public ReferenceType replaceUndefiniteGenericsToDefinite(IClassInfo classinfo, GenericParameters<GenericDeclarationType> parameters) {
		ReferenceType definiteGenericType = DefiniteGenericType.fromNullableDeclaration(
				parameters.findOrGetGenericType(name, () -> classinfo.findGenericType(name).orElse(null))
		);
		
		return definiteGenericType != null ? definiteGenericType : this;
	}
	
	@Override
	public ReferenceType replaceAllTypes(@Immutable Map<GenericDeclarationType, ReferenceType> replaceTable) {
		return replaceTable.entrySet().stream()
				.filter(entry -> entry.getKey().getName().equals(name))
				.findAny().map(Map.Entry::getValue).orElse(this);
	}
}
