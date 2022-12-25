package x590.javaclass.instruction.scope;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.DisassemblerContext;
import x590.javaclass.scope.EmptyInfiniteLoopScope;
import x590.javaclass.scope.IfScope;
import x590.javaclass.scope.Scope;

public class GotoInstruction extends EndPosInstruction {
	
	public GotoInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public Scope toScope(DecompilationContext context) {
		int endIndex = context.posToIndex(endPos);
		int currentIndex = context.currentIndex();
		Scope currentScope = context.currentScope();
		
		if(endIndex > currentIndex) {
			
			if(currentScope instanceof IfScope currentIf) {
				if(recognizeElse(context, endIndex, currentIndex, currentIf))
					return null;
			}
			
		} else if(endIndex < currentIndex) {
			// loop
			
		} else {
			return new EmptyInfiniteLoopScope(context);
		}
		
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
}