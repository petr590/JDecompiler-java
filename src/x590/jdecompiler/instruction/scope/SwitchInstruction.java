package x590.jdecompiler.instruction.scope;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.scope.Scope;
import x590.jdecompiler.scope.SwitchScope;

public class SwitchInstruction extends ScopeInstruction {
	
	private final int defaultPos;
	private final Int2IntMap offsetTable;
	
	public SwitchInstruction(DisassemblerContext context, int defaultOffset, Int2IntMap offsetTable) {
		this.defaultPos = context.currentPos() + defaultOffset;
		this.offsetTable = offsetTable;
	}
	
	@Override
	protected Scope toScope(DecompilationContext context) {
		return new SwitchScope(context, context.posToIndex(defaultPos), offsetTable);
	}
}
