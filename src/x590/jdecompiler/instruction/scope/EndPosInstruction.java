package x590.jdecompiler.instruction.scope;

import x590.jdecompiler.context.DisassemblerContext;

/**
 * Хранит endPos для scope-а, потом он преобразуется в индекс
 */
public abstract class EndPosInstruction extends ScopeInstruction {
	
	public final int endPos;
	
	public EndPosInstruction(int endPos) {
		this.endPos = endPos;
	}
	
	public EndPosInstruction(DisassemblerContext context, int offset) {
		this.endPos = context.currentPos() + offset;
	}
}
