package x590.jdecompiler.operationinstruction;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.Operation;

/**
 * Класс, описывающий объект, который является одновременно и операцией, и инструкцией.
 */
public abstract class OperationInstruction extends Operation implements Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return this;
	}
}
