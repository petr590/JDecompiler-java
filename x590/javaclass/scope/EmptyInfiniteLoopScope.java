package x590.javaclass.scope;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.jdecompiler.JDecompiler;

public class EmptyInfiniteLoopScope extends Scope {
	
	public EmptyInfiniteLoopScope(DecompilationContext context) {
		super(context, context.currentIndex());
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.write(JDecompiler.getInstance().canOmitCurlyBrackets() ? "for(;;);" : " for(;;) {}");
	}
}