package x590.jdecompiler.operation.cmp;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.BooleanOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.Type;

public abstract class CmpOperation extends BooleanOperation {
	
	public final Operation operand1, operand2;
	
	public CmpOperation(Type requiredType, DecompilationContext context) {
		this.operand2 = context.popAsNarrowest(requiredType);
		this.operand1 = context.popAsNarrowest(requiredType);
	}
	
	@Override
	public final void writeTo(StringifyOutputStream out, StringifyContext context) {
		throw new UnsupportedOperationException("Method writeTo(StringifyOutputStream out, StringifyContext context) must not call on CmpOperation");
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof CmpOperation operation &&
				operand1.equals(operation.operand1) && operand2.equals(operation.operand2);
	}
}