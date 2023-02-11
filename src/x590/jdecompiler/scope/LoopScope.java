package x590.jdecompiler.scope;

import java.util.List;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.condition.AndOperation;
import x590.jdecompiler.operation.condition.ConditionOperation;
import x590.jdecompiler.operation.condition.OrOperation;

public class LoopScope extends ConditionalScope {
	
	private boolean isWhile;
	
	public LoopScope(DecompilationContext context, int startIndex, int endIndex, ConditionOperation condition) {
		super(context, startIndex, endIndex, condition);
	}
	
	public LoopScope(DecompilationContext context, int startIndex, int endIndex, ConditionOperation condition, int conditionStartIndex) {
		super(context, startIndex, endIndex, condition, conditionStartIndex);
	}
	
	public LoopScope(DecompilationContext context, Scope superScope, int startIndex, int endIndex, ConditionOperation condition) {
		super(context, startIndex, endIndex, condition);
		init(startIndex, endIndex, superScope);
	}
	
	public LoopScope(DecompilationContext context, Scope superScope, int startIndex, int endIndex, ConditionOperation condition, int conditionStartIndex) {
		super(context, startIndex, endIndex, condition, conditionStartIndex);
		init(startIndex, endIndex, superScope);
	}
	
	
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
		
		Scope currentScope = superScope();
		
		if(currentScope instanceof IfScope ifScope && ifScope.endIndex() == context.currentIndex() + 1) {
//			Logger.debug("IIIIIIIIIIIIIIFFFFFFFFFFF!!!!!!!!!!!!!");
			
			ifScope.remove();
			setCondition(new AndOperation(ifScope.getCondition(), getCondition()));
			
			currentScope = ifScope.superScope();
		}
		
		List<Operation> operations = this.getOperations();
		
		if(operations.size() == 1 && operations.get(0) instanceof LoopScope loopScope && loopScope.startIndex() == startIndex()) {
			setCondition(new OrOperation(loopScope.getCondition(), getCondition()));
			setConditionStartIndex(loopScope.conditionStartIndex());
			
			loopScope.remove();
			deleteRemovedOperations();
			
			this.addOperations(loopScope.getOperations(), loopScope.startIndex());
		}
	}
	
	
	@Override
	public void finalizeScope(DecompilationContext context) {
		super.finalizeScope(context);
		update(context);
	}
	
	
	public void makeWhileLoop() {
		isWhile = true;
	}
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		if(isWhile) {
			writeWhilePart(out, context);
		} else {
			out.write("do");
		}
	}
	
	@Override
	public void writeBack(StringifyOutputStream out, StringifyContext context) {
		
		if(!isWhile) {
			if(canOmitCurlyBrackets())
				out.println().printIndent();
			else
				out.writesp();
			
			writeWhilePart(out, context);
			out.write(';');
		}
	}
	
	private void writeWhilePart(StringifyOutputStream out, StringifyContext context) {
		out.print("while(").print(getCondition(), context).print(')');
	}
}