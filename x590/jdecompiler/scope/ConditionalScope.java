package x590.jdecompiler.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.condition.ConditionOperation;

public abstract class ConditionalScope extends Scope {
	
	private ConditionOperation condition;
	private final int conditionStartIndex;
	
	public ConditionalScope(DecompilationContext context, int endIndex, ConditionOperation condition) {
		super(context, endIndex);
		this.condition = condition;
		this.conditionStartIndex = context.currentExpressionStartIndex();
	}
	
	public ConditionalScope(DecompilationContext context, int startIndex, int endIndex, ConditionOperation condition) {
		super(context, startIndex, endIndex);
		this.condition = condition;
		this.conditionStartIndex = context.currentExpressionStartIndex();
	}
	
	public ConditionOperation getCondition() {
		return condition;
	}
	
	public void setCondition(ConditionOperation condition) {
		this.condition = condition;
	}
	
	public int conditionStartIndex() {
		return conditionStartIndex;
	}
}