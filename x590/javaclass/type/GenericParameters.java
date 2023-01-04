package x590.javaclass.type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

import javax.annotation.Nullable; 

import x590.javaclass.ClassInfo;
import x590.javaclass.Stringified;
import x590.javaclass.StringWritableAndImportable;
import x590.javaclass.exception.InvalidTypeNameException;
import x590.javaclass.io.ExtendedStringReader;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.util.Util;

public class GenericParameters<T extends ReferenceType> implements Stringified, StringWritableAndImportable {
	
	public final List<T> types;
	
	public GenericParameters(ExtendedStringReader in, IntFunction<? extends T> supplier) {
		in.mark();
		
		if(in.read() != '<')
			throw new InvalidTypeNameException(in, 0);
		
		List<T> types = new ArrayList<>();
		
		for(int ch; (ch = in.get()) != '>';) {
			types.add(supplier.apply(ch));
		}
		
		this.types = Collections.unmodifiableList(types);
		
		in.incPos();
		in.unmark();
	}
	
	public static <T extends ReferenceType> @Nullable GenericParameters<T> read(ExtendedStringReader in, IntFunction<? extends T> supplier) {
		return in.get() == '<' ? new GenericParameters<>(in, supplier) : null;
	}
	
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
		Util.forEachExcludingLast(types, type -> out.write(type, classinfo), () -> out.write(", "));
		out.write('>');
	}
}
