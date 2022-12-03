package x590.javaclass.scope;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;

public class ElseScope extends IfElseScope {
	
	protected final IfScope ifScope;
	
	public ElseScope(DecompilationContext context, int endIndex, IfScope ifScope) {
		super(context, endIndex);
		this.ifScope = ifScope;
		ifScope.elseScope = this;
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
		return out;
	}
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		out.write(canOmitCurlyBrackets() ? "else" : " else");
	}
}