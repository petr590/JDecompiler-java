package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.ArrayType;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;

public abstract class ArrayStoreOperation extends Operation {
	
	private final Operation array, index, value;
	
	public ArrayStoreOperation(ArrayType requiredType, DecompilationContext context) {
		this.value = context.stack.pop();
		this.index = context.stack.popAsNarrowest(PrimitiveType.INT);
		this.array = context.stack.pop();
		
		Type elementType = array.getReturnTypeAsNarrowest(requiredType).elementType;
		
		value.castReturnTypeToNarrowest(elementType);
		
		if(array.original() instanceof NewArrayOperation newArray) {
			newArray.initializer.add(this);
			this.remove();
		}
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print(array, context).print('[').print(index, context).print("] = ").print(value, context);
	}
	
	@Override
	public void writeAsArrayInitializer(StringifyOutputStream out, StringifyContext context) {
		value.writeAsArrayInitializer(out, context);
	}
	
	@Override
	public Type getReturnType() {
		return PrimitiveType.VOID;
	}
	
	@Override
	public boolean requiresLocalContext() {
		return !this.isRemoved() && array.requiresLocalContext() || index.requiresLocalContext() || value.requiresLocalContext();
	}
}