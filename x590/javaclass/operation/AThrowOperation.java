package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.ClassType;

public class AThrowOperation extends VoidOperation {
	
	private final Operation throwable;
	
	public AThrowOperation(DecompilationContext context) {
		this.throwable = context.stack.popAsNarrowest(ClassType.THROWABLE);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print("throw ").print(throwable, context);
	}
}