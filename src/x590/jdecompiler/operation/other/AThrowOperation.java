package x590.jdecompiler.operation.other;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.AbstractOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.VoidOperation;
import x590.jdecompiler.type.reference.ClassType;

public final class AThrowOperation extends AbstractOperation implements VoidOperation {
	
	private final Operation throwable;
	
	public AThrowOperation(DecompilationContext context) {
		this.throwable = context.popAsNarrowest(ClassType.THROWABLE);
	}
	
	public Operation getOperand() {
		return throwable;
	}
	
	@Override
	public boolean isTerminable() {
		return true;
	}
	
	@Override
	public boolean canInlineInLambda() {
		return false;
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.printsp("throw").print(throwable, context);
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof AThrowOperation operation && throwable.equals(operation.throwable);
	}
}
