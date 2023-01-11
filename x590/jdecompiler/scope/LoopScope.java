package x590.jdecompiler.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.condition.ConditionOperation;

public class LoopScope extends ConditionalScope {
	
	private boolean isWhile;
	
	public LoopScope(DecompilationContext context, int startIndex, int endIndex, ConditionOperation condition) {
		super(context, startIndex, endIndex, condition);
	}
	
	public void makeWhileLoop() {
		isWhile = true;
	}
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		if(isWhile) {
			writeWhilePart(out, context);
		} else {
			out.write("do");
		}
	}
	
	@Override
	public StringifyOutputStream printBack(StringifyOutputStream out, StringifyContext context) {
		
		if(!isWhile) {
			if(canOmitCurlyBrackets())
				out.println().printIndent();
			else
				out.writesp();
			
			writeWhilePart(out, context);
			return out.print(';');
		}
		
		return out;
	}
	
	private void writeWhilePart(StringifyOutputStream out, StringifyContext context) {
		out.print("while(").print(getCondition(), context).print(')');
	}
}