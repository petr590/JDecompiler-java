package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;
import x590.javaclass.type.TypeSize;

public class PopOperation extends Operation {
	
	public final Operation value;
	
	public PopOperation(TypeSize size, DecompilationContext context) {
		this.value = context.stack.popWithSize(size);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		value.writeTo(out, context);
	}
	
	@Override
	public Type getReturnType() {
		return PrimitiveType.VOID;
	}
}