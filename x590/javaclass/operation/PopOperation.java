package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.TypeSize;

public class PopOperation extends VoidOperation {
	
	public final Operation value;
	
	public PopOperation(TypeSize size, DecompilationContext context) {
		this.value = context.stack.popWithSize(size);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		value.writeTo(out, context);
	}
}