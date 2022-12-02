package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.XorOperatorOperation;
import x590.javaclass.type.Type;
import x590.javaclass.operation.Operation;

public class XorOperatorInstruction extends OperatorInstruction {
	
	public XorOperatorInstruction(Type type) {
		super(type);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new XorOperatorOperation(type, context);
	}
}