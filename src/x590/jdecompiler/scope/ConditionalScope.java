package x590.jdecompiler.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.condition.ConditionOperation;
import x590.util.Logger;

public abstract class ConditionalScope extends Scope {
	
	private ConditionOperation condition;
	private int conditionStartIndex;
	
	public ConditionalScope(DecompilationContext context, int endIndex, ConditionOperation condition) {
		super(context, endIndex);
		this.condition = condition;
		this.conditionStartIndex = context.currentExpressionStartIndex();
		Logger.debug("conditionStartIndex = " + conditionStartIndex + " for " + this);
	}
	
	public ConditionalScope(DecompilationContext context, int startIndex, int endIndex, ConditionOperation condition) {
		super(context, startIndex, endIndex);
		this.condition = condition;
		this.conditionStartIndex = context.currentExpressionStartIndex();
		Logger.debug("conditionStartIndex = " + conditionStartIndex + " for " + this);
	}
	
	public ConditionalScope(DecompilationContext context, int startIndex, int endIndex, ConditionOperation condition, int conditionStartIndex) {
		super(context, startIndex, endIndex);
		this.condition = condition;
		this.conditionStartIndex = conditionStartIndex;
	}
	
	public ConditionOperation getCondition() {
		return condition;
	}
	
	public void setCondition(ConditionOperation condition) {
		this.condition = condition;
	}
	
	public void setConditionStartIndex(int conditionStartIndex) {
		this.conditionStartIndex = conditionStartIndex;
	}
	
	public int conditionStartIndex() {
		return conditionStartIndex;
	}
}
