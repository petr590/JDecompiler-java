package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.ALoadOperation;
import x590.javaclass.operation.Operation;

public class ALoadInstruction extends LoadInstruction {
	
	public ALoadInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new ALoadOperation(context, index);
	}
}