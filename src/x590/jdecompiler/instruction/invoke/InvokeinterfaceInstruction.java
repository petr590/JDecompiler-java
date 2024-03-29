package x590.jdecompiler.instruction.invoke;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.invoke.InvokeinterfaceOperation;
import x590.util.IntegerUtil;

public final class InvokeinterfaceInstruction extends InvokeInstruction {
	
	public InvokeinterfaceInstruction(DisassemblerContext context, int index, int count, int zeroByte) {
		super(index);
		
		if(count == 0) {
			context.warning("illegal format of instruction invokeinterface at pos 0x" + IntegerUtil.hex(context.currentPos()) +
					": by specification, third byte must not be zero");
		}
		
		if(zeroByte != 0) {
			context.warning("illegal format of instruction invokeinterface at pos 0x" + IntegerUtil.hex(context.currentPos()) +
					": by specification, fourth byte must be zero");
		}
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new InvokeinterfaceOperation(context, index);
	}
}
