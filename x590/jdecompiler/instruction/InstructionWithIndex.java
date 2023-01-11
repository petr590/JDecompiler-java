package x590.jdecompiler.instruction;

public abstract class InstructionWithIndex extends Instruction {
	
	public final int index;
	
	public InstructionWithIndex(int index) {
		this.index = index;
	}
}
