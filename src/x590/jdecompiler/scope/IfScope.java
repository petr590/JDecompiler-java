package x590.jdecompiler.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.instruction.scope.IfInstruction;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.condition.ConditionOperation;
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
		if(IfInstruction.recognizeIfScope(context, superScope(), conditionStartIndex(), endIndex(), this::getCondition)) {
			this.remove();
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
					elseScope == null ||
					getCode().size() != 1 ||
					!(getCode().get(0) instanceof IfScope)
				);
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
