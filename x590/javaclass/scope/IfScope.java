package x590.javaclass.scope;

import javax.annotation.Nullable;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.compare.ConditionOperation;

public class IfScope extends IfElseScope {
	
	private final ConditionOperation condition;
	protected @Nullable ElseScope elseScope;
	
	
	public IfScope(DecompilationContext context, int endIndex, ConditionOperation condition) {
		super(context, endIndex);
		this.condition = condition.invert();
	}
	
	
	@Override
	public void addOperation(DecompilationContext context, Operation operation) {
		if(operation != elseScope) {
			super.addOperation(context, operation);
		} else {
			context.getSuperScope().addOperation(context, operation);
		}
	}
	
	
	@Override
	protected boolean canOmitCurlyBrackets() {
		return canSelfOmitCurlyBrackets() && (elseScope == null || elseScope.canSelfOmitCurlyBrackets());
	}
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		out.print("if(").print(condition, context).print(')');
	}
}