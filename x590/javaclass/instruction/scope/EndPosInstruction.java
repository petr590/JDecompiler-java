package x590.javaclass.instruction.scope;

import x590.javaclass.context.DisassemblerContext;

/**
 * Хранит endPos для скопа, потом он преобразуется в индекс
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