package x590.jdecompiler.operationinstruction;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.AbstractOperation;
import x590.jdecompiler.operation.Operation;

/**
 * Класс, описывающий объект, который является одновременно и операцией, и инструкцией.
 */
public abstract class OperationInstruction extends AbstractOperation implements Instruction {
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return this;
	}
	
	// Одинаковые методы из Instruction и Operation требуют перезаписи
	@Override
	public void postDecompilation(DecompilationContext context) {}
}
