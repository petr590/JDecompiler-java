package x590.jdecompiler.instruction.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.context.PreDecompilationContext;
import x590.jdecompiler.operation.Operation;
import x590.util.annotation.Nullable;

/**
 * Хранит endPos для scope-а, потом он преобразуется в индекс
 */
public abstract class TransitionInstruction extends ScopeInstruction {
	
	protected final int fromPos, targetPos;
	
	protected int
			fromIndex = NONE_INDEX,
			targetIndex = NONE_INDEX;
	
	public TransitionInstruction(DisassemblerContext context, int offset) {
		this.fromPos = context.currentPos();
		this.targetPos = fromPos + offset;
	}
	
	public int getTargetPos() {
		return targetPos;
	}
	
	
	@Override
	public void preDecompilation(PreDecompilationContext context) {
		
		assert context.currentPos() == fromPos;
		
		this.fromIndex = context.posToIndex(fromPos);
		this.targetIndex = context.posToIndex(targetPos);
	}
	
	/** Запускается при декомпиляции на индексе на один меньше, чем индекс, соответствующий {@link #targetPos} */
	public abstract @Nullable Operation toOperationBeforeTargetIndex(DecompilationContext context);
}
