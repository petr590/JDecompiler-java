package x590.jdecompiler.scope;

import java.util.Collections;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;

public class FinallyScope extends CatchScope {
	
	public FinallyScope(DecompilationContext context, int endIndex, boolean hasNext) {
		super(context, endIndex, Collections.emptyList(), hasNext);
	}
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		out.write("finally");
	}
}
