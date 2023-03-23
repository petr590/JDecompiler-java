package x590.jdecompiler.instruction.scope;

import java.util.Optional;

import x590.jdecompiler.attribute.CodeAttribute.ExceptionTable.TryEntry;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.context.PreDecompilationContext;
import x590.jdecompiler.operation.condition.BooleanConstOperation;
import x590.jdecompiler.scope.CatchScope;
import x590.jdecompiler.scope.ElseScope;
import x590.jdecompiler.scope.EmptyInfiniteLoopScope;
import x590.jdecompiler.scope.IfScope;
import x590.jdecompiler.scope.LoopScope;
import x590.jdecompiler.scope.Scope;
import x590.util.annotation.Nullable;

public class GotoInstruction extends TransitionInstruction {
	
	private int fromIndex = -1, targetIndex = -1;
	
	private Role role;
	
	public enum Role {
		UNKNOWN, ELSE, CATCH_OVERJUMP,
		EMPTY_INFINITE_LOOP, INFINITE_LOOP, WHILE_LOOP_PROLOGUE
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
	public void preDecompilation(PreDecompilationContext context) {
		
		assert context.currentPos() == fromPos;
		
		this.fromIndex = context.posToIndex(fromPos);
		this.targetIndex = context.posToIndex(targetPos);
		
		if(role == Role.UNKNOWN) {
			
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
	}
	
	
	@Override
	public @Nullable Scope toScopeAtTargetPos(DecompilationContext context) {
		if(role == Role.INFINITE_LOOP) {
			return new LoopScope(context, targetIndex, fromIndex, BooleanConstOperation.TRUE, true);
		}
		
		return null;
	}
	
	@Override
	public Scope toScope(DecompilationContext context) {
		
		context.addBreak(targetIndex - 1);
		
		return switch(role) {
			
			case UNKNOWN -> {
				if(recognizeElse(context, context.currentScope())) {
					yield null;
				}
				
				if(role == Role.UNKNOWN) {
					if(context.currentScope() instanceof CatchScope catchScope &&
							context.currentIndex() + 1 == catchScope.endIndex()) {
						
						role = Role.CATCH_OVERJUMP;
						yield null;
					}
				}
				
				yield null;
			}
			
			case ELSE -> {
				if(!recognizeElse(context, context.currentScope())) {
					context.warning("Cannot recognize else scope");
				}
				
				yield null;
			}
			
			case INFINITE_LOOP -> {
//				if(context.currentScope() instanceof IfScope ifScope &&
//						targetIndex == ifScope.conditionStartIndex() && context.currentIndex() + 1 == ifScope.endIndex()) {
//					yield new LoopScope(context, ifScope);
//				}
				
//				yield new LoopScope(context, targetIndex, context.currentIndex(), BooleanConstOperation.TRUE);
				yield null;
			}
			
			case EMPTY_INFINITE_LOOP -> new EmptyInfiniteLoopScope(context);
			
			case CATCH_OVERJUMP, WHILE_LOOP_PROLOGUE -> null;
			
			default -> throw new IllegalArgumentException("Unexpected value: " + role);
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
			int endIndexM1 = targetIndex - 1;
			
			Optional<LoopScope> foundScope = context.getOperations().stream()
					.filter(
						operation -> !operation.isRemoved() && operation instanceof LoopScope scope &&
								scope.startIndex() == currentIndexP1 && scope.conditionStartIndex() == endIndexM1
							
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
