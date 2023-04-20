package x590.jdecompiler.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import x590.jdecompiler.Importable;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.exception.InvalidSignatureException;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.writable.SameDisassemblingStringifyWritable;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nonnull;
import x590.util.annotation.Nullable;

public final class GenericParameters<T extends ReferenceType> implements SameDisassemblingStringifyWritable<ClassInfo>, Importable {
	
	private final @Immutable List<T> types;
	
	public GenericParameters(ExtendedStringInputStream in, Function<ExtendedStringInputStream, ? extends T> supplier) {
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
	
	public static <T extends ReferenceType> @Nonnull GenericParameters<T> readNonnull(ExtendedStringInputStream in, Function<ExtendedStringInputStream, ? extends T> supplier) {
		if(in.get() == '<')
			return new GenericParameters<>(in, supplier);
		
		throw new InvalidSignatureException(in);
	}
	
	public static <T extends ReferenceType> @Nullable GenericParameters<T> readNullable(ExtendedStringInputStream in, Function<ExtendedStringInputStream, ? extends T> supplier) {
		return in.get() == '<' ? new GenericParameters<>(in, supplier) : null;
	}
	
	
	public @Immutable List<T> getTypes() {
		return types;
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImportsFor(types);
	}
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		out.print('<').printAllObjects(types, classinfo, ", ").print('>');
	}
	
	@Override
	public String toString() {
		return types.stream().map(Type::toString).collect(Collectors.joining(", ", "<", ">"));
	}
}
