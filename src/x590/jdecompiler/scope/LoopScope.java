package x590.jdecompiler.scope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.condition.ConditionOperation;
import x590.jdecompiler.operation.increment.IIncOperation;
import x590.jdecompiler.operation.store.StoreOperation;
import x590.jdecompiler.operation.variable.VariableDefinitionOperation;

public class LoopScope extends ConditionalScope {
	
	private boolean isPreCondition;
	
	private List<VariableDefinitionOperation> initializationOperations = Collections.emptyList();
	private List<Operation> incrementOperations = Collections.emptyList();
	
	public LoopScope(DecompilationContext context, int startIndex, int endIndex, ConditionOperation condition) {
		super(context, startIndex, endIndex, condition);
	}
	
	public LoopScope(DecompilationContext context, int startIndex, int endIndex, ConditionOperation condition, boolean isPreCondition) {
		super(context, startIndex, endIndex, condition);
		this.isPreCondition = isPreCondition;
	}
	
	public LoopScope(DecompilationContext context, int startIndex, int endIndex, ConditionOperation condition, int conditionStartIndex) {
		super(context, startIndex, endIndex, condition, conditionStartIndex);
	}
	
	@Deprecated
	public LoopScope(DecompilationContext context, Scope superScope, int startIndex, int endIndex, ConditionOperation condition, int conditionStartIndex) {
		super(context, startIndex, endIndex, condition, conditionStartIndex);
		init(conditionStartIndex, endIndex, superScope);
	}
	
	
	@Deprecated
	private void init(int startIndex, int endIndex, Scope superScope) {
		
		// Мы определяем цикл уже в конце, поэтому мы должны
		// добавить все операции в тело цикла из внешнего scope
		if(superScope.startIndex() <= startIndex)
			this.addOperations(superScope.pullOperationsFromIndex(startIndex), startIndex);
	}
	
	
	public void setConditionAndUpdate(ConditionOperation condition, DecompilationContext context) {
		setCondition(condition);
		update(context);
	}
	
	
	public void update(DecompilationContext context) {
		
		List<Operation> operations = this.getOperations();
		
		if(operations.size() == 1) {
			
			Operation operation = operations.get(0);
			
			if(operation instanceof IfScope ifScope && ifScope.endIndex() == context.currentIndex()) {
				
				setCondition(getCondition().and(ifScope.getCondition()));
				
				ifScope.removeOnlySelf();
				deleteRemovedOperations();
				
				addOperationsFrom(ifScope);
				
				update(context);
				
			} else if(operation instanceof LoopScope loopScope && loopScope.startIndex() == startIndex()) {
				
				setCondition(loopScope.getCondition().or(getCondition()));
				setConditionStartIndex(loopScope.conditionStartIndex());
				
				loopScope.remove();
				deleteRemovedOperations();
				
				addOperationsFrom(loopScope);
				
				update(context);
			}
		}
	}
	
	
	private void resolveInitializationOperations() {
		var initializationOperations = this.initializationOperations = new ArrayList<>();
		
		List<Operation> operations = superScope().getOperations();
		VariableDefinitionOperation prevOperation = null;
		
		int thisIndex = operations.indexOf(this);
		
		for(var iter = operations.listIterator(thisIndex != -1 ? thisIndex : operations.size() - 1);
				iter.hasPrevious() && iter.previous() instanceof VariableDefinitionOperation operation;) {
			
			boolean variableDefinition = operation.isVariableDefinition();
			
			if(prevOperation == null ||
					variableDefinition == prevOperation.isVariableDefinition() &&
					(!variableDefinition || operation.getVariable().getType().equals(prevOperation.getVariable().getType()))) {
				
				initializationOperations.add(0, operation);
				operation.getVariable().makeAnIndex();
				operation.removeFromScope();
				
				if(prevOperation != null) {
					prevOperation.hideTypeDefinition();
				}
			}
			
			prevOperation = operation;
		}
	}
	
	private void resolveIncrementOperations() {
		var incrementOperations = this.incrementOperations = new ArrayList<>();
		
		List<Operation> operations = getOperations();
		
		for(var iter = operations.listIterator(operations.size()); iter.hasPrevious();) {
			
			Operation operation = iter.previous();
			
			if(operation instanceof IIncOperation ||
				operation instanceof StoreOperation store && !store.isVariableDefinition()) {
				
				incrementOperations.add(0, operation);
				operation.removeFromScope();
			} else {
				break;
			}
		}
	}
	
	
	@Override
	public void finalizeScope(DecompilationContext context) {
		super.finalizeScope(context);
		update(context);
	}
	
	public void makePreCondition() {
		isPreCondition = true;
	}
	
	@Override
	public void postDecompilation(DecompilationContext context) {
		if(!isPreCondition && getCondition().isAlwaysTrue()) {
			isPreCondition = true;
		}
		
		if(isPreCondition) {
			
			if(getOperationsCount() == 1 && getOperationAt(0) instanceof IfScope ifScope &&
					conditionStartIndex() == ifScope.conditionStartIndex() && endIndex() + 1 == ifScope.endIndex()) {
				
				ifScope.removeOnlySelf();
				deleteRemovedOperations();
				addOperationsFrom(ifScope);
				setCondition(getCondition().and(ifScope.getCondition()));
			}
			
			resolveInitializationOperations();
			resolveIncrementOperations();
			deleteRemovedOperations();
			
			superScope().deleteRemovedOperations();
		}
	}
	
	
	@Override
	public boolean isBreakable() {
		return true;
	}
	
	@Override
	public boolean isContinuable() {
		return true;
	}
	
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		if(isPreCondition) {

			var condition = getCondition();
			
			if(!initializationOperations.isEmpty() || !incrementOperations.isEmpty()) {
				out.print("for(").printAll(initializationOperations, context, ", ").print(';');
				
				if(!condition.isAlwaysTrue())
					out.printsp().print(condition, context);
				
				out.write(';');
				
				if(!incrementOperations.isEmpty())
					out.printsp().printAll(incrementOperations, context, ", ");
				
				out.write(')');
				
			} else if(condition.isAlwaysTrue()) {
				out.write("for(;;)");
				
			} else {
				writeWhilePart(out, context);
			}
			
		} else {
			out.write("do");
		}
	}
	
	@Override
	public void writeBack(StringifyOutputStream out, StringifyContext context) {
		
		if(!isPreCondition) {
			
			if(canOmitCurlyBrackets())
				out.println().printIndent();
			else
				out.printsp();
			
			writeWhilePart(out, context);
			out.write(';');
		}
	}
	
	private void writeWhilePart(StringifyOutputStream out, StringifyContext context) {
		out.print("while(").print(getCondition(), context).print(')');
	}
}
