package x590.jdecompiler.operation.dup;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.TypeSize;

public abstract class AbstractDupOperation extends Operation {
	
	protected final Operation value;
	
	public AbstractDupOperation(TypeSize size, DecompilationContext context) {
		this.value = context.stack.peekWithSize(size);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.write(value, context);
	}
	
	@Override
	public Type getReturnType() {
		return value.getReturnType();
	}
	
	@Override
	public void allowImplicitCast() {
		value.allowImplicitCast();
	}
	
	@Override
	public void onCastReturnType(Type newType) {
		value.onCastReturnType(newType);
	}
	
	@Override
	public Operation original() {
		return value.original();
	}
	
	@Override
	public void addVariableName(String name) {
		value.addVariableName(name);
	}
	
	@Override
	public boolean requiresLocalContext() {
		return value.requiresLocalContext();
	}
	
	@Override
	public int getPriority() {
		return value.getPriority();
	}
}
