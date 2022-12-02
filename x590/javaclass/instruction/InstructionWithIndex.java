package x590.javaclass.instruction;

public abstract class InstructionWithIndex extends Instruction {
	
	public final int index;
	
	public InstructionWithIndex(int index) {
		this.index = index;
	}
}