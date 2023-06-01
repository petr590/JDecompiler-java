package x590.jdecompiler.type.reference.generic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import x590.jdecompiler.Importable;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.exception.InvalidSignatureException;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.jdecompiler.writable.SameDisassemblingStringifyWritable;
import x590.util.annotation.Immutable;

public final class GenericParameters<T extends ReferenceType>
		implements SameDisassemblingStringifyWritable<ClassInfo>, Importable, Iterable<T> {
	
	private static final GenericParameters<?> EMPTY = new GenericParameters<>(Collections.emptyList());
	private final @Immutable List<T> types;
	
	private GenericParameters(@Immutable List<T> types) {
		this.types = types;
	}
	
	private GenericParameters(ExtendedStringInputStream in, Function<? super ExtendedStringInputStream, ? extends T> supplier) {
		in.mark();
		
		if(in.read() != '<')
			throw new InvalidSignatureException(in, 0);
		
		List<T> types = new ArrayList<>();
		
		while(in.get() != '>') {
			types.add(supplier.apply(in));
		}
		
		this.types = Collections.unmodifiableList(types);
		
		in.incPos();
		in.unmark();
	}
	
	
	public static <T extends ReferenceType> GenericParameters<T> readNonempty(ExtendedStringInputStream in, Function<? super ExtendedStringInputStream, ? extends T> supplier) {
		if(in.get() == '<')
			return new GenericParameters<>(in, supplier);
		
		throw new InvalidSignatureException(in);
	}
	
	public static <T extends ReferenceType> GenericParameters<T> readEmptyable(ExtendedStringInputStream in, Function<? super ExtendedStringInputStream, ? extends T> supplier) {
		return in.get() == '<' ? new GenericParameters<>(in, supplier) : empty();
	}
	
	public static <T extends ReferenceType> GenericParameters<T> of(@Immutable List<T> types) {
		return types.isEmpty() ? empty() : new GenericParameters<>(types);
	}
	
	@SafeVarargs
	public static <T extends ReferenceType> GenericParameters<T> of(T... types) {
		return new GenericParameters<>(List.of(types));
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends ReferenceType> GenericParameters<T> empty() {
		return (GenericParameters<T>)EMPTY;
	}
	
	
	public boolean isEmpty() {
		return types.isEmpty();
	}
	
	
	public @Immutable List<T> getTypes() {
		return types;
	}
	
	
	public Optional<T> findGenericType(String name) {
		return types.stream().filter(type -> type.getName().equals(name)).findAny();
	}
	
	public T findOrGetGenericType(String name, Supplier<T> defaultSupplier) {
		return findGenericType(name).orElseGet(defaultSupplier);
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImportsFor(types);
	}
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		out.print('<').printAllObjects(types, classinfo, ", ").print('>');
	}
	
	public StringBuilder writeToStringBuilder(StringBuilder stringBuilder) {
		stringBuilder.append('<');
		
		for(var parameter : types)
			stringBuilder.append(parameter.getEncodedName());
			
		return stringBuilder.append('>');
	}
	
	public String getEncodedNameFor(String classEncodedName) {
		return writeToStringBuilder(new StringBuilder(classEncodedName)).toString();
	}


	public GenericParameters<ReferenceType> replaceUndefiniteGenericsToDefinite(ClassInfo classinfo, GenericParameters<GenericDeclarationType> parameters) {
		return replaceTypes(type -> type.replaceUndefiniteGenericsToDefinite(classinfo, parameters));
	}
	
	public GenericParameters<ReferenceType> replaceAllTypes(Map<GenericDeclarationType, ReferenceType> replaceTable) {
		return replaceTypes(type -> type.replaceAllTypes(replaceTable));
	}

	@SuppressWarnings("unchecked")
	private GenericParameters<ReferenceType> replaceTypes(Function<? super ReferenceType, ReferenceType> replacer) {
		List<ReferenceType> replacedTypes = types.stream().map(replacer).toList();
		return replacedTypes.equals(types) ? (GenericParameters<ReferenceType>) this : of(replacedTypes);
	}
	
	
	@Override
	public Iterator<T> iterator() {
		return types.iterator();
	}
	
	public T get(int index) {
		return types.get(index);
	}
	
	public int size() {
		return types.size();
	}
	
	
	@Override
	public String toString() {
		return types.stream().map(Type::toString).collect(Collectors.joining(", ", "<", ">"));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object other) {
		return other instanceof GenericParameters<?> && this.equals((GenericParameters<T>)other);
	}
	
	public boolean equals(GenericParameters<T> other) {
		return types.equals(other.types);
	}
	
	@Override
	public int hashCode() {
		return types.hashCode();
	}
}
