package x590.jdecompiler.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.operator.TernaryOperatorOperation;

public class ElseScope extends IfElseScope {
	
	protected final IfScope ifScope;
	
	protected ElseScope(DecompilationContext context, int endIndex, IfScope ifScope) {
		super(context, endIndex, ifScope.superScope());
		this.ifScope = ifScope;
	}
	
	@Override
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
		if(ifScope.isEmpty() && this.isEmpty() && context.stack.size() >= 2) {
			context.stack.push(new TernaryOperatorOperation(ifScope.getCondition(), context));
			this.remove();
			ifScope.remove();
		}
	}
}