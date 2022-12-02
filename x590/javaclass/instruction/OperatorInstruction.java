package x590.javaclass.instruction;

import x590.javaclass.type.Type;

public abstract class OperatorInstruction extends Instruction {
	
	protected final Type type;
	
	public OperatorInstruction(Type type) {
		this.type = type;
	}
}