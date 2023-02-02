package x590.jdecompiler.instruction.constant;

import x590.jdecompiler.constpool.ConstValueConstant;
import x590.jdecompiler.instruction.Instruction;

public abstract class ConstInstruction<C extends ConstValueConstant> extends Instruction {
	
	protected final C constant;
	
	public ConstInstruction(C constant) {
		this.constant = constant;
	}
}
