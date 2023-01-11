package x590.jdecompiler.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.condition.AndOperation;
import x590.jdecompiler.operation.condition.ConditionOperation;
import x590.jdecompiler.operation.condition.OrOperation;
import x590.util.annotation.Nullable;

public class IfScope extends ConditionalScope {
	
	private @Nullable ElseScope elseScope;
	
	public IfScope(DecompilationContext context, int endIndex, ConditionOperation condition) {
		super(context, endIndex, condition.invert());
	}
	
	
	public void setConditionAndUpdate(ConditionOperation condition, DecompilationContext context) {
		setCondition(condition);
		update(context);
	}
	
	/** Обновляет scope: если он является вторым условием оператора or или and,
	 * то условие superScope изменяется, и этот scope удаляется */
	public void update(DecompilationContext context) {
		if(this.superScope() instanceof IfScope ifScope) {
			
			if(ifScope.endIndex() == this.endIndex()) {
				ifScope.setConditionAndUpdate(new AndOperation(ifScope.getCondition(), getCondition()), context);
				this.remove();
				
			} else if(ifScope.endIndex() == context.currentIndex() + 1) {
				
				ifScope.setEndIndex(this.endIndex());
				ifScope.setConditionAndUpdate(new OrOperation(ifScope.getCondition().invert(), getCondition()), context);
				this.remove();
			}
		}
	}
	
	
	public void addElse(DecompilationContext context, int endIndex) {
		if(this.elseScope != null)
			throw new IllegalArgumentException("Cannot set another elseScope");
		
		this.elseScope = new ElseScope(context, endIndex, this);
		this.superScope().addOperation(elseScope, context);
		context.addScopeToQueue(elseScope);
	}

	
	protected boolean canSelfOmitCurlyBrackets() {
		return super.canOmitCurlyBrackets();
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