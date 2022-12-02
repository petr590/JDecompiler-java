package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.Type;

public abstract class ReturnOperation extends VoidOperation {
	
	private final Operation operand;
	
	public ReturnOperation(Type requiredType, DecompilationContext context) {
		this.operand = context.stack.popAsNarrowest(requiredType);
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print("return ").print(operand, context);
	}
}