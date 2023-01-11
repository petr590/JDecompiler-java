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
		this.operand1 = context.stack.popAsNarrowest(requiredType);
		this.operand2 = context.stack.popAsNarrowest(requiredType);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		throw new UnsupportedOperationException("Cannot call toString(StringifyContext) method on CmpOperation");
	}
}