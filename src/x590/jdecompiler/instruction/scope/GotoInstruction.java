package x590.jdecompiler.instruction.scope;

import java.util.List;
import java.util.Optional;

import x590.jdecompiler.attribute.CodeAttribute.ExceptionTable.TryEntry;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.context.PreDecompilationContext;
import x590.jdecompiler.operation.BreakOperation;
import x590.jdecompiler.operation.ContinueOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.condition.BooleanConstOperation;
import x590.jdecompiler.scope.CatchScope;
import x590.jdecompiler.scope.ElseScope;
import x590.jdecompiler.scope.EmptyInfiniteLoopScope;
import x590.jdecompiler.scope.IfScope;
import x590.jdecompiler.scope.LoopScope;
import x590.jdecompiler.scope.Scope;
import x590.util.annotation.Nullable;

public class GotoInstruction extends TransitionInstruction {
	
	private int fromIndex = NONE_INDEX, targetIndex = NONE_INDEX;
	
	private Role role;
	
	public enum Role {
		UNKNOWN, ELSE, CATCH_OVERJUMP,
		EMPTY_INFINITE_LOOP, INFINITE_LOOP, WHILE_LOOP_PROLOGUE,
		BREAK, CONTINUE
	}
	
	public GotoInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
		
		this.role =
				offset == 0 ? Role.EMPTY_INFINITE_LOOP :
				offset < 0 ? Role.INFINITE_LOOP :
				Role.UNKNOWN;
	}
	
	
	public Role getRole() {
		return role;
	}
	
	
	@Override
	@SuppressWarnings("incomplete-switch")
	public void preDecompilation(PreDecompilationContext context) {
		
		assert context.currentPos() == fromPos;
		
		this.fromIndex = context.posToIndex(fromPos);
		this.targetIndex = context.posToIndex(targetPos);
		
		switch(role) {
			case UNKNOWN -> {
			
				if(targetPos > context.currentPos() && context.hasIfInstructionsPointedTo(context.currentIndex() + 1)) {
					role = Role.ELSE;
					
				} else {
					final int currentIndex = context.currentIndex();
					
					Optional<TryEntry> foundTryEntry = context.getExceptionTable().getEntries().stream()
							.filter(tryEntry -> currentIndex == tryEntry.getFactualEndIndex(context)).findAny();
					
					if(foundTryEntry.isPresent()) {
						role = Role.CATCH_OVERJUMP;
						foundTryEntry.get().setLastPos(targetPos);
					}
				}
			}
			
			case INFINITE_LOOP -> {
				
				if(targetPos < context.currentPos()) {
					
					List<GotoInstruction> otherGotos = context.getGotoInstructionsPointedTo(targetIndex);
					
					if(!otherGotos.isEmpty()) {
						otherGotos.forEach(gotoInstruction -> gotoInstruction.role = Role.CONTINUE);
					}
					
					assert !otherGotos.contains(this);
					assert otherGotos.stream()
							.mapToInt(gotoInstruction -> gotoInstruction.fromIndex)
							.max().orElse(NONE_INDEX) < fromIndex;
				}
			}
		}
	}
	
	
	@Override
	public @Nullable Operation toOperationAtTargetPos(DecompilationContext context) {
		if(role == Role.INFINITE_LOOP)
			return new LoopScope(context, targetIndex, fromIndex + 1, BooleanConstOperation.TRUE, true);
		
		return null;
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		
		context.saveStackState(targetIndex);
		
		int targetIndexM1 = targetIndex - 1;
		
		context.addBreak(targetIndexM1);
		
		return switch(role) {
			
			case UNKNOWN -> {
				
				Operation breakOperation = recognizeBreak(context);
				if(breakOperation != null) {
					yield breakOperation;
				}
				
				if(recognizeElse(context, context.currentScope())) {
					yield null;
				}
				
				if(context.currentScope() instanceof CatchScope catchScope &&
						context.currentIndex() + 1 == catchScope.endIndex()) {
					
					role = Role.CATCH_OVERJUMP;
				}
				
				yield null;
			}
			
			case ELSE -> {
				
				Operation breakOperation = recognizeBreak(context);
				if(breakOperation != null) {
					yield breakOperation;
				}
				
				if(!recognizeElse(context, context.currentScope())) {
					context.warning("Cannot recognize else scope");
				}
				
				yield null;
			}
			
			case CONTINUE -> {
				
				boolean hasOtherContinuable = false;
				int targetIndex = this.targetIndex;
				
				for(Scope scope = context.currentScope(); scope != null; scope = scope.superScope()) {
					
					if(scope.isContinuable()) {
						if(scope.startIndex() == targetIndex) {
							yield new ContinueOperation(scope, hasOtherContinuable);
						}
						
						hasOtherContinuable = true;
					}
				}
				
				context.warning("Cannot recognize continue operation");
				yield null;
			}
			
			case EMPTY_INFINITE_LOOP -> new EmptyInfiniteLoopScope(context);
			
			case CATCH_OVERJUMP, WHILE_LOOP_PROLOGUE, INFINITE_LOOP, BREAK -> null;
		};
		
		// Old code
		
//		int endIndex = this.endIndex = context.posToIndex(endPos);
//		int currentIndex = context.currentIndex();
//		Scope currentScope = context.currentScope();
//		
//		if(endIndex > currentIndex) {
//			
//			if(currentScope instanceof IfScope currentIf) {
//				if(recognizeElse(context, endIndex, currentIndex, currentIf)) {
//					return apply();
//				}
//				
//			} else if(currentScope instanceof ElseScope elseScope &&
//					elseScope.superScope() instanceof IfScope currentIf) {
//				
//				if(recognizeElse(context, endIndex, currentIndex, currentIf)) {
//					elseScope.setEndIndex(currentIf.endIndex() - 1);
//					
//					return apply();
//				}
//			}
//			
//		} else if(endIndex < currentIndex) {
//			Logger.debug("Loop");
//			
//		} else {
//			return apply(new EmptyInfiniteLoopScope(context));
//		}
//		
//		context.addBreak(endIndex - 1);
//		
//		return null;
	}
	
	@Override
	protected @Nullable Scope toScope(DecompilationContext context) {
		return toOperation(context) instanceof Scope scope ? scope : null;
	}
	
	private @Nullable Operation recognizeBreak(DecompilationContext context) {
		boolean hasOtherBreakable = false;
		int targetIndex = this.targetIndex;
		
		for(Scope scope = context.currentScope(); scope != null; scope = scope.superScope()) {
			
			if(scope.isBreakable()) {
				if(scope.expandTo(targetIndex)) {
					role = Role.BREAK;
					return new BreakOperation(scope, hasOtherBreakable);
				}
				
				hasOtherBreakable = true;
			}
		}
		
		return null;
	}
	
	private boolean recognizeElse(DecompilationContext context, Scope currentScope) {
		
		if(currentScope instanceof IfScope currentIf) {
			return recognizeElse(context, currentIf);
			
		} else if(currentScope instanceof ElseScope elseScope && targetIndex == elseScope.endIndex()) {
			elseScope.setEndIndex(context.currentIndex() + 1);
			return recognizeElse(context, elseScope.superScope());
		}
		
		return false;
	}
	
	private boolean recognizeElse(DecompilationContext context, IfScope currentIf) {
		
		int currentIndex = context.currentIndex();
		
		if(currentIndex + 1 == currentIf.endIndex()) { // Создаём else
			currentIf.addElse(context, targetIndex);
			return true;
		}
		
		
		/*
			Для такого кода компилятор оптимизирует goto на goto:
			
			if(condition1) {
				// Здесь if указывает не на конец первого if-а, а на начало code3
				if(condition2) {
					code1;
				}
			} else {
				code2;
			}
			
			code3;
		 */
		
		if(targetIndex == currentIf.endIndex() && currentIf.superScope() instanceof IfScope superIf) {
			currentIf.setEndIndex(currentIndex + 1);
			return recognizeElse(context, superIf);
		}
		
		return false;
	}
	
	@Override
	public void postDecompilation(DecompilationContext context) {
		if(role == Role.UNKNOWN) {
			int currentIndexP1 = context.currentIndex() + 1;
			int targetIndexM1 = targetIndex - 1;
			
			Optional<LoopScope> foundScope = context.getOperations().stream()
					.filter(
						operation -> !operation.isRemoved() && operation instanceof LoopScope scope &&
								scope.startIndex() == currentIndexP1 && scope.conditionStartIndex() == targetIndexM1
					
					).map(operation -> (LoopScope)operation).findAny();
			
			
			if(foundScope.isPresent()) {
				role = Role.WHILE_LOOP_PROLOGUE;
				foundScope.get().makePreCondition();
			} else {
				context.warning("The `goto " + targetIndex + "` instruction is not recognized");
			}
		}
	}
}
