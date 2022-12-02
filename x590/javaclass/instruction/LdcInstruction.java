package x590.javaclass.instruction;

import x590.javaclass.constpool.ConstValueConstant;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.operation.LdcOperation;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.TypeSize;

public class LdcInstruction extends InstructionWithIndex {
	
	private final TypeSize size;
	
	public LdcInstruction(TypeSize size, DisassemblerContext context, int index) {
		super(index);
		this.size = size;
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new LdcOperation<ConstValueConstant>(size, context, index);
	}
}