package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.Type;

public class CastOperation extends Operation {
	
	private final Operation value;
	protected final Type castedType;
	protected final boolean implicitCast;
	private boolean implicitCastAllowed = false;
	
	public CastOperation(Type requiredType, Type castedType, boolean implicitCast, DecompilationContext context) {
		this.value = context.stack.popAsNarrowest(requiredType);
		this.castedType = castedType;
		this.implicitCast = implicitCast;
	}
	
	@Override
	public void allowImplicitCast() {
		implicitCastAllowed = true;
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(implicitCast && implicitCastAllowed)
			out.write(value, context);
		else
			out.print('(').print(castedType.toString(context.classinfo)).print(')').print(value, context);
	}
	
	@Override
	public Type getReturnType() {
		return castedType;
	}
	
	@Override
	public boolean requiresLocalContext() {
		return value.requiresLocalContext();
	}
}