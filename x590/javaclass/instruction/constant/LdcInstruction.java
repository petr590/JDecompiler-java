package x590.javaclass.instruction.constant;

import x590.javaclass.constpool.ConstValueConstant;
import x590.javaclass.constpool.DoubleConstant;
import x590.javaclass.constpool.FloatConstant;
import x590.javaclass.constpool.IntegerConstant;
import x590.javaclass.constpool.LongConstant;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.instruction.InstructionWithIndex;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.constant.DConstOperation;
import x590.javaclass.operation.constant.FConstOperation;
import x590.javaclass.operation.constant.IConstOperation;
import x590.javaclass.operation.constant.LConstOperation;
import x590.javaclass.operation.constant.LdcOperation;
import x590.javaclass.type.TypeSize;

public class LdcInstruction extends InstructionWithIndex {
	
	private final TypeSize size;
	
	public LdcInstruction(TypeSize size, DisassemblerContext context, int index) {
		super(index);
		this.size = size;
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		ConstValueConstant value = context.pool.get(index);
		
		if(value instanceof IntegerConstant integerConstant) return new IConstOperation(integerConstant.value);
		if(value instanceof LongConstant    longConstant)    return new LConstOperation(longConstant.value);
		if(value instanceof FloatConstant   floatConstant)   return new FConstOperation(floatConstant.value);
		if(value instanceof DoubleConstant  doubleConstant)  return new DConstOperation(doubleConstant.value);
		
		return new LdcOperation<>(size, value);
	}
}