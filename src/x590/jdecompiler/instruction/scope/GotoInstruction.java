package x590.jdecompiler.instruction.scope;

import java.util.Optional;

import x590.jdecompiler.attribute.CodeAttribute.ExceptionTable.TryEntry;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.DecompilationContext.PreDecompilationContext;
import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.scope.CatchScope;
import x590.jdecompiler.scope.ElseScope;
import x590.jdecompiler.scope.EmptyInfiniteLoopScope;
import x590.jdecompiler.scope.IfScope;
import x590.jdecompiler.scope.Scope;

public class GotoInstruction extends InstructionWithEndPos {
	
	private int endIndex;
	
	private Role role;
	
	public enum Role {
		UNKNOWN, ELSE, CATCH_JUMPOVER,
		EMPTY_INFINITE_LOOP, INFINITE_LOOP
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
		if(role == Role.UNKNOWN) {
			
			if(endPos > context.currentPos() && context.hasIfInstructionsPointedTo(context.currentIndex() + 1)) {
				role = Role.ELSE;
				
			} else {
				final int currentPos = context.currentPos();
				
				Optional<TryEntry> foundTryEntry =
						context.getExceptionTable().getEntries().stream().filter(tryEntry -> tryEntry.getEndPos() == currentPos).findAny();
				
				if(foundTryEntry.isPresent()) {
					role = Role.CATCH_JUMPOVER;
					foundTryEntry.get().setLastPos(endPos);
				}
			}
		}
	}
	
	@Override
	public Scope toScope(DecompilationContext context) {
		
		this.endIndex = context.posToIndex(endPos);
		
		return switch(role) {
			
			case UNKNOWN, ELSE -> {
				if(recognizeElse(context, context.currentScope())) {
					yield null;
				}
				
				if(role == Role.UNKNOWN && context.currentScope() instanceof CatchScope catchScope &&
						context.currentIndex() + 1 == catchScope.endIndex()) {
					
					role = Role.CATCH_JUMPOVER;
					yield null;
				}
				
				if(role == Role.ELSE) {
					context.warning("Cannot recognize else scope");
				} else {
					context.warning("The `goto " + endIndex + "` instruction is not recognized");
				}
				
				yield null;
			}
			
			case CATCH_JUMPOVER -> null;
			case EMPTY_INFINITE_LOOP -> new EmptyInfiniteLoopScope(context);
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
			
		} else if(currentScope instanceof ElseScope elseScope && endIndex == elseScope.endIndex()) {
			elseScope.setEndIndex(context.currentIndex() + 1);
			return recognizeElse(context, elseScope.superScope());
		}
		
		return false;
	}
	
	private boolean recognizeElse(DecompilationContext context, IfScope currentIf) {
		
		int currentIndex = context.currentIndex();
		
		if(currentIndex + 1 == currentIf.endIndex()) { // Создаём else
			currentIf.addElse(context, endIndex);
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
		
		if(endIndex == currentIf.endIndex() && currentIf.superScope() instanceof IfScope superIf) {
			currentIf.setEndIndex(currentIndex + 1);
			return recognizeElse(context, superIf);
		}
		
		return false;
	}
	
	@Override
	public void postDecompilation(DecompilationContext context) {
//		if(!applied) {
//			int currentIndexP1 = context.currentIndex() + 1;
//			int endIndexM1 = endIndex - 1;
//			
//			Optional<LoopScope> foundScope = context.getOperations().stream()
//					.filter(
//						operation -> !operation.isRemoved() && operation instanceof LoopScope scope &&
//								scope.startIndex() == currentIndexP1 && scope.conditionStartIndex() == endIndexM1
//							
//					).map(operation -> (LoopScope)operation).findAny();
//			
//			
//			if(foundScope.isPresent()) {
//				apply();
//				foundScope.get().makeWhileLoop();
//			}
//		}
	}
}
