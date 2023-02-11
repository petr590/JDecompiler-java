package x590.jdecompiler.instruction.scope;

import java.util.Optional;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.scope.ElseScope;
import x590.jdecompiler.scope.EmptyInfiniteLoopScope;
import x590.jdecompiler.scope.IfScope;
import x590.jdecompiler.scope.LoopScope;
import x590.jdecompiler.scope.Scope;
import x590.util.Logger;

public class GotoInstruction extends InstructionWithEndPos {
	
	private int endIndex;
	
	/** Применена ли инструкция goto где-либо */
	private boolean applied;
	
	public GotoInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	private Scope apply() {
		applied = true;
		return null;
	}
	
	private Scope apply(Scope scope) {
		applied = true;
		return scope;
	}
	
	@Override
	public Scope toScope(DecompilationContext context) {
		int endIndex = this.endIndex = context.posToIndex(endPos);
		int currentIndex = context.currentIndex();
		Scope currentScope = context.currentScope();
		
		if(endIndex > currentIndex) {
			
			if(currentScope instanceof IfScope currentIf) {
				if(recognizeElse(context, endIndex, currentIndex, currentIf)) {
					return apply();
				}
				
			} else if(currentScope instanceof ElseScope elseScope &&
					elseScope.superScope() instanceof IfScope currentIf) {
				
				if(recognizeElse(context, endIndex, currentIndex, currentIf)) {
					elseScope.setEndIndex(currentIf.endIndex() - 1);
					
					return apply();
				}
			}
			
		} else if(endIndex < currentIndex) {
			Logger.debug("Loop");
			
		} else {
			return apply(new EmptyInfiniteLoopScope(context));
		}
		
		context.addBreak(endIndex - 1);
		
		return null;
	}
	
	private boolean recognizeElse(DecompilationContext context, int endIndex, int currentIndex, IfScope currentIf) {
		
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
			return recognizeElse(context, endIndex, currentIndex, superIf);
		}
		
		return false;
	}
	
	@Override
	public void postDecompilation(DecompilationContext context) {
		if(!applied) {
			int currentIndexP1 = context.currentIndex() + 1;
			int endIndexM1 = endIndex - 1;
			
			Optional<LoopScope> foundScope = context.getOperations().stream()
					.filter(
						operation -> !operation.isRemoved() && operation instanceof LoopScope scope &&
								scope.startIndex() == currentIndexP1 && scope.conditionStartIndex() == endIndexM1
							
					).map(operation -> (LoopScope)operation).findAny();
			
			
			if(foundScope.isPresent()) {
				apply();
				foundScope.get().makeWhileLoop();
			}
		}
	}
}
