package x590.jdecompiler.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.operator.TernaryOperatorOperation;

public class ElseScope extends Scope {
	
	protected final IfScope ifScope;
	
	protected ElseScope(DecompilationContext context, int endIndex, IfScope ifScope) {
		super(context, endIndex, ifScope.superScope());
		this.ifScope = ifScope;
	}

	
	protected boolean canSelfOmitCurlyBrackets() {
		return super.canOmitCurlyBrackets();
	}
	
	@Override
	protected boolean canOmitCurlyBrackets() {
		return canSelfOmitCurlyBrackets() && ifScope.canSelfOmitCurlyBrackets();
	}
	
	
	@Override
	public StringifyOutputStream printFront(StringifyOutputStream out, StringifyContext context) {
		return canOmitCurlyBrackets() ? out.println().printIndent() : out;
	}
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		out.write(canOmitCurlyBrackets() ? "else" : " else");
	}
	
	@Override
	public void finalizeScope(DecompilationContext context) {
		if(ifScope.isEmpty() && this.isEmpty() && context.stackSize() >= 2) {
			context.push(new TernaryOperatorOperation(ifScope.getCondition(), context));
			this.remove();
			ifScope.remove();
		}
	}
}