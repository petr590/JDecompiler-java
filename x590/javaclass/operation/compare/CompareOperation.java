package x590.javaclass.operation.compare;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.CmpOperation;
import x590.javaclass.operation.Operation;

public abstract class CompareOperation extends ConditionOperation {
	
	public static ConditionOperation valueOf(DecompilationContext context, CompareType compareType) {
		return valueOf(context.stack.pop(), compareType);
	}
	
	public static ConditionOperation valueOf(Operation operand, CompareType compareType) {
		return operand instanceof CmpOperation ?
				new CompareBinaryOperation((CmpOperation)operand, compareType) :
				new CompareWithZeroOperation(operand, compareType);
	}
	
	
	public CompareType compareType;
	
	public CompareOperation(CompareType compareType) {
		this.compareType = compareType;
	}
	
	@Override
	public int getPriority() {
		return compareType.getPriority();
	}
}