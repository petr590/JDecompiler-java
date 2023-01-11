package x590.jdecompiler.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;

public class EmptyInfiniteLoopScope extends Scope {
	
	public EmptyInfiniteLoopScope(DecompilationContext context) {
		super(context, context.currentIndex());
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.write(JDecompiler.getInstance().canOmitCurlyBrackets() ? "for(;;);" : " for(;;) {}");
	}
}
