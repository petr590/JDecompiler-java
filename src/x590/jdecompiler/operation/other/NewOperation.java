package x590.jdecompiler.operation.other;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.AbstractOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.reference.ClassType;

public final class NewOperation extends AbstractOperation {
	
	private final ClassType type;
	
	public NewOperation(DecompilationContext context, int index) {
		this(context.pool.getClassConstant(index).toClassType());
	}
	
	public NewOperation(ClassType type) {
		this.type = type;
	}
	
	public ClassType getType() {
		return type;
	}
	
	@Override
	public Type getReturnType() {
		return type;
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(type);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.printsp("new").print(type, context.getClassinfo());
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof NewOperation operation &&
				type.equals(operation.type);
	}
}
