package x590.jdecompiler.instruction.operator;

import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.type.Type;

public abstract class OperatorInstruction extends Instruction {
	
	protected final Type type;
	
	public OperatorInstruction(Type type) {
		this.type = type;
	}
}
