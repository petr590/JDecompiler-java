package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.operation.InvokeinterfaceOperation;
import x590.javaclass.operation.Operation;
import x590.javaclass.util.Util;

public class InvokeinterfaceInstruction extends InstructionWithIndex {
	
	public InvokeinterfaceInstruction(DisassemblerContext context, int index, int count, int zeroByte) {
		super(index);
		
		if(count == 0)
			context.warning("illegal format of instruction invokeinterface at pos 0x" + Util.hex(context.currentPos()) +
					": by specification, third byte must not be zero");
		
		if(zeroByte != 0)
			context.warning("illegal format of instruction invokeinterface at pos 0x" + Util.hex(context.currentPos()) +
					": by specification, fourth byte must be zero");
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new InvokeinterfaceOperation(context, index);
	}
}