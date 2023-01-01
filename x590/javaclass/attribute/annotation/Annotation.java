package x590.javaclass.attribute.annotation;

import java.util.Arrays;

import x590.javaclass.ClassInfo;
import x590.javaclass.StringWritableAndImportable;
import x590.javaclass.constpool.ConstantPool;
import x590.javaclass.io.ExtendedDataInputStream;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.ClassType;
import x590.javaclass.util.Util;

public class Annotation implements StringWritableAndImportable {
	
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
			Util.forEachExcludingLast(elements,
					element -> out.write(element, classinfo),
					element -> out.write(", "));
			out.write(')');
		 }
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(type);
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof Annotation annotation && this.equals(annotation);
	}
	
	public boolean equals(Annotation other) {
		return this == other ||  this.type.equals(other.type) && Arrays.equals(this.elements, other.elements);
	}
}