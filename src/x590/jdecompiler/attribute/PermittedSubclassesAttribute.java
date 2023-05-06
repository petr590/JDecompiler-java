package x590.jdecompiler.attribute;

import java.util.List;
import java.util.Objects;

import x590.jdecompiler.Importable;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.ConstantPool;
import x590.jdecompiler.io.ExtendedDataInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.writable.StringifyWritable;
import x590.util.annotation.Immutable;

public class PermittedSubclassesAttribute extends Attribute implements StringifyWritable<ClassInfo>, Importable {
	
	private final @Immutable List<ClassType> subclasses;
	
	protected PermittedSubclassesAttribute(String name, int length, ExtendedDataInputStream in, ConstantPool pool) {
		super(name, length);
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
		var thisType = classinfo.getThisType();
		var enclosingOrThisClass = Objects.requireNonNullElse(thisType.getEnclosingClass(), thisType);
		
		if(!subclasses.stream().allMatch(classType -> classType.isNestmateOf(enclosingOrThisClass)))
			out.print(" permits ").printAll(subclasses, classinfo, ", ");
	}
}
