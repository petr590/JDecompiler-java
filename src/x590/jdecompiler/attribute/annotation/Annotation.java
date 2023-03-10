package x590.jdecompiler.attribute.annotation;

import java.util.List;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.Importable;
import x590.jdecompiler.StringifyWritable;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.ClassType;
import x590.util.annotation.Immutable;

public final class Annotation implements StringifyWritable<ClassInfo>, Importable {
	
	private final ClassType type;
	private final @Immutable List<Element> elements;
	
	protected Annotation(ExtendedDataInputStream in, ConstantPool pool) {
		this.type = ClassType.fromTypeDescriptor(pool.getUtf8String(in.readUnsignedShort()));
		this.elements = in.readImmutableList(() -> new Element(in, pool));
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.print('@').print(type, classinfo);
		
		if(!elements.isEmpty()) {
			out.write('(');
			
			if(elements.size() == 1 && elements.get(0).getName().equals("value")) {
				out.print(elements.get(0).getValue(), classinfo);
			} else {
				out.printAll(elements, classinfo, ", ");
			}
			
			out.write(')');
		 }
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(type);
		classinfo.addImportsFor(elements);
	}
	
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof Annotation annotation && this.equals(annotation);
	}
	
	public boolean equals(Annotation other) {
		return this == other || type.equals(other.type) && elements.equals(other.elements);
	}
}
