package x590.jdecompiler.attribute.annotation;

import java.util.Arrays;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.StringWritableAndImportable;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.ClassType;
import x590.util.ArrayUtil;

public final class Annotation implements StringWritableAndImportable {
	
	private final ClassType type;
	private final Element[] elements;
	
	protected Annotation(ExtendedDataInputStream in, ConstantPool pool) {
		this.type = ClassType.fromTypeDescriptor(pool.getUtf8String(in.readUnsignedShort()));
		this.elements = in.readArray(Element[]::new, () -> new Element(in, pool));
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.print('@').print(type, classinfo);
		
		if(elements.length > 0) {
			out.write('(');
			ArrayUtil.forEachExcludingLast(elements,
					element -> out.write(element, classinfo),
					element -> out.write(", "));
			out.write(')');
		 }
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(type);
		ArrayUtil.forEach(elements, element -> element.addImports(classinfo));
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof Annotation annotation && this.equals(annotation);
	}
	
	public boolean equals(Annotation other) {
		return this == other ||  this.type.equals(other.type) && Arrays.equals(this.elements, other.elements);
	}
}
