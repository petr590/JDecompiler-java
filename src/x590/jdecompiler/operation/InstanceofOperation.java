package x590.jdecompiler.operation;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.ReferenceType;
import x590.jdecompiler.type.Types;

public final class InstanceofOperation extends AbstractOperation implements BooleanOperation {
	
	private final ReferenceType clazz;
	private final Operation object;
	
	public InstanceofOperation(DecompilationContext context, int index) {
		this.clazz = context.pool.getClassConstant(index).toReferenceType();
		this.object = context.popAsNarrowest(Types.ANY_OBJECT_TYPE);
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(clazz);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.printPrioritied(this, object, context, Associativity.LEFT).print(" instanceof ").print(clazz, context.getClassinfo());
	}
	
	@Override
	public int getPriority() {
		return Priority.INSTANCEOF;
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof InstanceofOperation operation &&
				clazz.equals(operation.clazz) && object.equals(operation.object);
	}
}
