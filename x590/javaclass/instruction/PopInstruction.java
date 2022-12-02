package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.PopOperation;
import x590.javaclass.type.TypeSize;

public class PopInstruction extends Instruction {
	
	private final TypeSize size;
	
	public PopInstruction(TypeSize size) {
		this.size = size;
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new PopOperation(size, context);
	}
}