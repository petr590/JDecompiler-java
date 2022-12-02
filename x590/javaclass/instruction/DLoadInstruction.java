package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.DLoadOperation;
import x590.javaclass.operation.Operation;

public class DLoadInstruction extends LoadInstruction {
	
	public DLoadInstruction(int index) {
		super(index);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new DLoadOperation(context, index);
	}
}