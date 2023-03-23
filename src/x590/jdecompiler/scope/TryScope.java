package x590.jdecompiler.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;

public class TryScope extends Scope {
	
	public TryScope(DecompilationContext context, int endIndex) {
		super(context, context.currentIndex() - 1, endIndex);
	}
	
	@Override
	protected boolean canOmitCurlyBrackets() {
		return false;
	}
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		out.write("try");
	}
	
	@Override
	public void writeSeparator(StringifyOutputStream out, StringifyContext context, Operation nextOperation) {}
}
