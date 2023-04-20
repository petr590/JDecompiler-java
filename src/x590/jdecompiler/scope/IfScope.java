package x590.jdecompiler.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.instruction.scope.IfInstruction;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.condition.ConditionOperation;
import x590.util.annotation.Nullable;

public class IfScope extends ConditionalScope {
	
	private @Nullable ElseScope elseScope, prevElseScope;
	
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
		if(IfInstruction.recognizeIfScope(context, superScope(), conditionStartIndex(), endIndex(), this::getCondition)) {
			this.remove();
		}
	}
	
	
	public void addElse(DecompilationContext context, int endIndex) {
		if(elseScope != null)
			throw new IllegalArgumentException("Cannot set another elseScope");
		
		elseScope = new ElseScope(context, endIndex, this);
		superScope().addOperation(elseScope, context);
		context.addScopeToQueue(elseScope);
	}
	
	
	void setPrevElse(ElseScope prevElseScope) {
		this.prevElseScope = prevElseScope;
	}
	
	
	@Override
	public boolean isTerminable() {
		return false;
	}
	
	boolean canSelfOmitCurlyBrackets() {
		return super.canOmitCurlyBrackets() && (
					/*
					 Для кода такого вида мы не можем опустить фигурные скобки,
					 иначе будет синтаксически другое выражение:
					
						if(condition1) {
							if(condition2)
								code1;
							
						} else {
							code2;
						}
					
					 Для избегания этой ситуации я написал код ниже:
					*/
					(elseScope == null || getOperationsCount() != 1 || !(getOperationAt(0) instanceof IfScope))
				);
	}
	
	boolean canSelfOmitCurlyBracketsForward() {
		return canSelfOmitCurlyBrackets() &&
				(elseScope == null || elseScope.canSelfOmitCurlyBracketsForward());
	}
	
	boolean canSelfOmitCurlyBracketsBackward() {
		return canSelfOmitCurlyBrackets() &&
				(prevElseScope == null || prevElseScope.canSelfOmitCurlyBracketsBackward());
	}
	
	@Override
	protected boolean canOmitCurlyBrackets() {
		return canSelfOmitCurlyBracketsForward() &&
				(prevElseScope == null || prevElseScope.canSelfOmitCurlyBracketsBackward());
	}
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		out.print("if(").print(getCondition(), context).print(')');
	}
	
	@Override
	public void writeSeparator(StringifyOutputStream out, StringifyContext context, Operation nextOperation) {
		if(elseScope == null)
			super.writeSeparator(out, context, nextOperation);
	}
}
