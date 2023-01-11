package x590.jdecompiler.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.condition.AndOperation;
import x590.jdecompiler.operation.condition.ConditionOperation;
import x590.jdecompiler.operation.condition.OrOperation;
import x590.util.annotation.Nullable;

public class IfScope extends IfElseScope {
	
	private ConditionOperation condition;
	protected @Nullable ElseScope elseScope;
	
	
	public IfScope(DecompilationContext context, int endIndex, ConditionOperation condition) {
		super(context, endIndex);
		this.condition = condition.invert();
	}
	
	
	public ConditionOperation getCondition() {
		return condition;
	}
	
	public void setConditionAndUpdate(ConditionOperation condition, DecompilationContext context) {
		this.condition = condition;
		update(context);
	}
	
	public void setCondition(ConditionOperation condition) {
		this.condition = condition;
	}
	
	/** Обновляет scope: если он является вторым условием оператора or или and,
	 * то условие superScope изменяется, и этот scope удаляется */
	public void update(DecompilationContext context) {
		if(superScope instanceof IfScope ifScope) {
			
			if(ifScope.endIndex() == endIndex) {
				ifScope.setConditionAndUpdate(new AndOperation(ifScope.getCondition(), condition), context);
				this.remove();
				
			} else if(ifScope.endIndex() == context.currentIndex() + 1) {
				
				ifScope.setEndIndex(endIndex);
				ifScope.setConditionAndUpdate(new OrOperation(ifScope.getCondition().invert(), condition), context);
				this.remove();
			}
		}
	}
	
	
	public void addElse(DecompilationContext context, int endIndex) {
		if(this.elseScope != null)
			throw new IllegalArgumentException("Cannot set another elseScope");
		
		this.elseScope = new ElseScope(context, endIndex, this);
		superScope.addOperation(elseScope);
		context.addScopeToQueue(elseScope);
	}
	
	
	@Override
	protected boolean canOmitCurlyBrackets() {
		return canSelfOmitCurlyBrackets() && (elseScope == null || elseScope.canSelfOmitCurlyBrackets());
	}
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		out.print("if(").print(getCondition(), context).print(')');
	}
	
	@Override
	public void writeSeparator(StringifyOutputStream out, StringifyContext context) {
		if(elseScope == null)
			super.writeSeparator(out, context);
	}
}