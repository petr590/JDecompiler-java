package x590.jdecompiler.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.StringWritableAndImportable;
import x590.jdecompiler.Stringified;
import x590.jdecompiler.exception.InvalidSignatureException;
import x590.jdecompiler.exception.InvalidTypeNameException;
import x590.jdecompiler.io.ExtendedStringReader;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.util.Util;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nonnull;
import x590.util.annotation.Nullable;

public class GenericParameters<T extends ReferenceType> implements Stringified, StringWritableAndImportable {
	
	public final @Immutable List<T> types;
	
	public GenericParameters(ExtendedStringReader in, Function<ExtendedStringReader, ? extends T> supplier) {
		in.mark();
		
		if(in.read() != '<')
			throw new InvalidTypeNameException(in, 0);
		
		List<T> types = new ArrayList<>();
		
		while(in.get() != '>') {
			types.add(supplier.apply(in));
		}
		
		this.types = Collections.unmodifiableList(types);
		
		in.incPos();
		in.unmark();
	}
	
	public static <T extends ReferenceType> @Nonnull GenericParameters<T> readNonnull(ExtendedStringReader in, Function<ExtendedStringReader, ? extends T> supplier) {
		if(in.get() == '<')
			return new GenericParameters<>(in, supplier);
		
		throw new InvalidSignatureException(in);
	}
	
	public static <T extends ReferenceType> @Nullable GenericParameters<T> readNullable(ExtendedStringReader in, Function<ExtendedStringReader, ? extends T> supplier) {
		return in.get() == '<' ? new GenericParameters<>(in, supplier) : null;
	}
	
	@Override
	public String toString() {
		return "<" + types.stream().map(type -> type.toString()).collect(Collectors.joining(", ")) + ">";
	}
	
	@Override
	public String toString(ClassInfo classinfo) {
		return "<" + types.stream().map(type -> type.toString(classinfo)).collect(Collectors.joining(", ")) + ">";
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		types.forEach(type -> type.addImports(classinfo));
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.write('<');
		Util.forEachExcludingLast(types, type -> out.write(type, classinfo), type -> out.write(", "));
		out.write('>');
	}
}
