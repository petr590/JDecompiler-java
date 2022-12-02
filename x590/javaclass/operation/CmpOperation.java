package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.Type;

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