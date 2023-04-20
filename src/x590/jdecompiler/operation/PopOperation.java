package x590.jdecompiler.operation;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.TypeSize;
import x590.jdecompiler.variable.Variable;
import x590.util.annotation.Nullable;

public final class PopOperation extends AbstractOperation implements VoidOperation, PossibleExceptionStoreOperation {
	
	public final Operation operand;
	
	public PopOperation(TypeSize size, DecompilationContext context) {
		this.operand = context.popWithSize(size);
		removeIfExceptionLoadOperation(context, operand);
	}
	
	
	public Operation getOperand() {
		return operand;
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		operand.writeTo(out, context);
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof PopOperation operation &&
				operand.equals(operation.operand);
	}
	
	@Override
	public @Nullable Variable getStoringVariable() {
		return null;
	}
}
