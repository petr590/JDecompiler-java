package x590.jdecompiler.attribute;

import java.util.List;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.Importable;
import x590.jdecompiler.StringifyWritable;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.ClassType;
import x590.util.annotation.Immutable;

public class PermittedSubclassesAttribute extends Attribute implements StringifyWritable<ClassInfo>, Importable {
	
	private final @Immutable List<ClassType> subclasses;
	
	protected PermittedSubclassesAttribute(int nameIndex, String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(nameIndex, name, length);
		this.subclasses = in.readImmutableList(() -> pool.getClassConstant(in.readUnsignedShort()).toClassType());
	}
	
	public @Immutable List<ClassType> getSubclasses() {
		return subclasses;
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImportsFor(subclasses);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, ClassInfo classinfo) {
		out.print(" permits ").printAll(subclasses, classinfo, ", ");
	}
}
