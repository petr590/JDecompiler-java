package x590.jdecompiler.instruction;

public abstract class InstructionWithIndex implements Instruction {
	
	public final int index;
	
	public InstructionWithIndex(int index) {
		this.index = index;
	}
}
