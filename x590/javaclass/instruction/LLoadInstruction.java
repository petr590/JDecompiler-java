package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.LLoadOperation;
import x590.javaclass.operation.Operation;

public class LLoadInstruction extends LoadInstruction {
	
	public LLoadInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new LLoadOperation(context, index);
	}
}