package x590.jdecompiler.operation;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.ClassType;

public class AThrowOperation extends VoidOperation {
	
	private final Operation throwable;
	
	public AThrowOperation(DecompilationContext context) {
		this.throwable = context.popAsNarrowest(ClassType.THROWABLE);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print("throw ").print(throwable, context);
	}
}
