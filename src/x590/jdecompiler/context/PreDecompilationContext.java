package x590.jdecompiler.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.instruction.scope.GotoInstruction;
import x590.jdecompiler.instruction.scope.IfInstruction;
import x590.jdecompiler.instruction.scope.TransitionInstruction;
import x590.jdecompiler.method.JavaMethod;
import x590.util.Logger;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public final class PreDecompilationContext extends DecompilationAndStringifyContext {
	
	static final @Immutable List<TransitionInstruction> DEFAULT_TRANSITION_INSTRUCTIONS_LIST = Collections.emptyList();
	static final @Immutable List<?> SINGLE_LIST = Collections.singletonList(null);
	
	@SuppressWarnings("unchecked")
	static final @Immutable List<IfInstruction> IF_INSTRUCTIONS_SINGLE_LIST = (List<IfInstruction>)SINGLE_LIST;
	
	@SuppressWarnings("unchecked")
	static final @Immutable List<GotoInstruction> GOTO_INSTRUCTIONS_SINGLE_LIST = (List<GotoInstruction>)SINGLE_LIST;
	
	private final Int2ObjectMap<List<TransitionInstruction>> transitionInstructions = new Int2ObjectOpenHashMap<>();
	private final Int2ObjectMap<List<IfInstruction>>         ifInstructions         = new Int2ObjectOpenHashMap<>();
	private final Int2ObjectMap<List<GotoInstruction>>       gotoInstructions       = new Int2ObjectOpenHashMap<>();
	
	public PreDecompilationContext(Context otherContext, ClassInfo classinfo, JavaMethod method, List<Instruction> instructions) {
		
		super(otherContext, classinfo, method);
		
		for(Instruction instruction : instructions) {
			
			instruction.preDecompilation(this);
			
			if(instruction instanceof TransitionInstruction transitionInstruction) {
				int instructionIndex = posToIndex(transitionInstruction.getTargetPos());
				
				transitionInstructions.computeIfAbsent(instructionIndex, key -> new ArrayList<>()).add(transitionInstruction);
				
				if(transitionInstruction instanceof IfInstruction ifInstruction) {
					ifInstructions.computeIfAbsent(instructionIndex, key -> new ArrayList<>()).add(ifInstruction);
				} else if(transitionInstruction instanceof GotoInstruction gotoInstruction) {
					gotoInstructions.computeIfAbsent(instructionIndex, key -> new ArrayList<>()).add(gotoInstruction);
				}
			}
			
			index++;
		}
	}
	
	Int2ObjectMap<List<TransitionInstruction>> getTransitionInstructions() {
		return transitionInstructions;
	}
	
	
	public @Immutable List<IfInstruction> getIfInstructionsPointedTo(int index) {
		return ifInstructions.getOrDefault(index, Collections.emptyList());
	}
	
	public @Nullable IfInstruction getIfInstructionPointedTo(int index) {
		return ifInstructions.getOrDefault(index, IF_INSTRUCTIONS_SINGLE_LIST).get(0);
	}
	
	public boolean hasIfInstructionsPointedTo(int index) {
		return ifInstructions.containsKey(index);
	}
	
	
	public @Immutable List<GotoInstruction> getGotoInstructionsPointedTo(int index) {
		return gotoInstructions.getOrDefault(index, Collections.emptyList());
	}
	
	public @Nullable GotoInstruction getGotoInstructionPointedTo(int index) {
		return gotoInstructions.getOrDefault(index, GOTO_INSTRUCTIONS_SINGLE_LIST).get(0);
	}
	
	public boolean hasGotoInstructionsPointedTo(int index) {
		return gotoInstructions.containsKey(index);
	}
	
	
	@Override
	public void warning(String message) {
		Logger.warning("Pre-decompilation warning: " + message);
	}
}
