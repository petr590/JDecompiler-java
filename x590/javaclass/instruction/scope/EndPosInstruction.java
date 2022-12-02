package x590.javaclass.instruction.scope;

public abstract class EndPosInstruction extends ScopeInstruction {
	
	public final int endPos;
	
	public EndPosInstruction(int endPos) {
		this.endPos = endPos;
	}
}