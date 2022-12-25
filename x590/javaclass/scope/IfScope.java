package x590.javaclass.scope;

import javax.annotation.Nullable;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.compare.ConditionOperation;

public class IfScope extends IfElseScope {
	
	private final ConditionOperation condition;
	protected @Nullable ElseScope elseScope;
	
	
	public IfScope(DecompilationContext context, int endIndex, ConditionOperation condition) {
		super(context, endIndex);
		this.condition = condition.invert();
	}
	
	
	public ConditionOperation getCondition() {
		return condition;
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