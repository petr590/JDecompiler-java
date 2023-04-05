package x590.jdecompiler.instruction.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.operation.Operation;
import x590.util.annotation.Nullable;

/**
 * Хранит endPos для scope-а, потом он преобразуется в индекс
 */
public abstract class TransitionInstruction extends ScopeInstruction {
	
	protected final int fromPos, targetPos;
	
	public TransitionInstruction(DisassemblerContext context, int offset) {
		this.fromPos = context.currentPos();
		this.targetPos = fromPos + offset;
	}
	
	public int getTargetPos() {
		return targetPos;
	}
	
	public abstract @Nullable Operation toOperationAtTargetPos(DecompilationContext context);
}
