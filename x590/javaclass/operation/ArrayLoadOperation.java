package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.ArrayType;
import x590.javaclass.type.Type;

public abstract class ArrayLoadOperation extends Operation {
	
	private final Operation array, index;
	private final Type elementType;
	
	public ArrayLoadOperation(ArrayType requiredType, DecompilationContext context) {
		this.index = context.stack.pop();
		this.array = context.stack.pop();
		
		this.elementType = array.getReturnTypeAsNarrowest(requiredType).elementType;
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print(array, context).print('[').print(index, context).print(']');
	}
	
	@Override
	public Type getReturnType() {
		return elementType;
	}
	
	@Override
	public boolean requiresLocalContext() {
		return array.requiresLocalContext() || index.requiresLocalContext();
	}
}