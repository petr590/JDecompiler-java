package x590.javaclass.scope;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.compare.ConditionOperation;

public class IfScope extends Scope {
	
	private final ConditionOperation condition;
	
	
	public IfScope(DecompilationContext context, int endIndex, ConditionOperation condition) {
		super(context, endIndex);
		this.condition = condition.invert();
	}
	
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		out.print("if(").print(condition, context).print(')');
	}
}