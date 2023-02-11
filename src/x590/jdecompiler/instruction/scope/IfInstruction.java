package x590.jdecompiler.instruction.scope;

import java.util.List;
import java.util.function.Supplier;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.condition.AndOperation;
import x590.jdecompiler.operation.condition.CompareOperation;
import x590.jdecompiler.operation.condition.CompareType;
import x590.jdecompiler.operation.condition.ConditionOperation;
import x590.jdecompiler.operation.condition.OrOperation;
import x590.jdecompiler.scope.IfScope;
import x590.jdecompiler.scope.LoopScope;
import x590.jdecompiler.scope.Scope;
import x590.util.Logger;
import x590.util.annotation.Nullable;

public abstract class IfInstruction extends InstructionWithEndPos {
	
	public IfInstruction(DisassemblerContext context, int offset) {
		super(context, offset);
	}
	
	@Override
	public Scope toScope(DecompilationContext context) {
		int endIndex = context.posToIndex(endPos);
		Scope currentScope = context.currentScope();
		
		ConditionOperation condition = getCondition(context);
		
		// Здесь endIndex по факту является startIndex для LoopScope, так как указывает назад
		if(endIndex < context.currentIndex()) {
			
			{
				LoopScope scope = recognizeIfScopeInLoop(context, currentScope, null, endIndex, condition);
				Logger.debug(scope);
				if(scope != null)
					return scope;
			}
			
			if(currentScope.startIndex() <= endIndex) {
				List<Operation> operations = currentScope.getOperationsFromIndex(endIndex);
				
				if(operations.size() == 1 && operations.get(0) instanceof LoopScope loopScope && loopScope.startIndex() == endIndex) {
					loopScope.setConditionAndUpdate(new OrOperation(loopScope.getCondition(), condition), context);
					return null;
				}
			}
			
			return new LoopScope(context, currentScope, endIndex, context.currentIndex(), condition);
		}
		
		
		if(recognizeIfScope(context, currentScope, context.currentExpressionStartIndex(), endIndex, () -> condition.invert())) {
			return null;
		}
		
		return new IfScope(context, endIndex, condition);
	}
	
	
	public static @Nullable LoopScope recognizeIfScopeInLoop(DecompilationContext context, Scope currentScope, @Nullable IfScope prevIfScope, int endIndex, ConditionOperation condition) {
		
		if(currentScope instanceof IfScope ifScope && currentScope.endIndex() == context.currentIndex() + 1) {
			ifScope.remove();
			
			// В сложной конфигурации, в ifScope есть ещё один цикл, если его не обнаружить, то он просто будет удалён вместе с ifScope
			for(Operation operation : ifScope.getOperations()) {
				if(operation instanceof LoopScope loopScope && loopScope.startIndex() == endIndex) {
					condition = new OrOperation(loopScope.getCondition(), condition);
				}
			}
			
			context.updateScopes();
			currentScope = context.currentScope();
			
			return recognizeIfScopeInLoop(context, currentScope, ifScope, endIndex, new AndOperation(ifScope.getCondition(), condition));
			
		} else {
			if(prevIfScope == null)
				return null;
			
			return new LoopScope(context, currentScope, endIndex, context.currentIndex(), condition, prevIfScope.conditionStartIndex());
		}
	}
	
	
//	TODO
//	public static boolean recognizeLoopScope(DecompilationContext context, Scope currentScope, int endIndex, Supplier<ConditionOperation> conditionGetter) {
//		IfScope ifScope;
//		
//		if(currentScope instanceof IfScope && currentScope.endIndex() == context.currentIndex() + 1) {
//			ifScope = (IfScope)currentScope;
//			ifScope.remove();
//			context.updateScopes();
//			currentScope = context.currentScope();
//			
//		} else {
//			ifScope = null;
//		}
//		
//		List<Operation> operations = currentScope.getOperationsFromIndex(endIndex);
//		
//		if(operations.size() == 1 && operations.get(0) instanceof LoopScope loopScope && loopScope.startIndex() == endIndex) {
//			loopScope.setCondition(new OrOperation(loopScope.getCondition(), conditionGetter.get()));
//			return true;
//		}
//		
//		return false;
//	}
	
	
	public static boolean recognizeIfScope(DecompilationContext context, Scope currentScope, int conditionStartIndex, int endIndex, Supplier<ConditionOperation> conditionGetter) {
		if(currentScope instanceof IfScope ifScope) {
			
			if(ifScope.endIndex() == endIndex && ifScope.startIndex() == conditionStartIndex) {
				ifScope.setConditionAndUpdate(new AndOperation(ifScope.getCondition(), conditionGetter.get()), context);
				return true;
				
			} else if(ifScope.endIndex() == context.currentIndex() + 1) {
				ifScope.setEndIndex(endIndex);
				ifScope.setConditionAndUpdate(new OrOperation(ifScope.getCondition().invert(), conditionGetter.get()), context);
				return true;
			}
		}
		
		return false;
	}
	
	
	public ConditionOperation getCondition(DecompilationContext context) {
		return CompareOperation.valueOf(context, getCompareType());
	}
	
	public abstract CompareType getCompareType();
}