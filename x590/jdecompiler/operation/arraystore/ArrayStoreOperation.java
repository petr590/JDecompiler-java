package x590.jdecompiler.operation.arraystore;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.anew.NewArrayOperation;
import x590.jdecompiler.type.ArrayType;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;

public abstract class ArrayStoreOperation extends Operation {
	
	private final Operation array, index, value;
	
	public ArrayStoreOperation(ArrayType requiredType, DecompilationContext context) {
		this.value = context.stack.pop();
		this.index = context.stack.popAsNarrowest(PrimitiveType.INT);
		this.array = context.stack.pop();
		
		Type elementType = array.getReturnTypeAsNarrowest(requiredType).getElementType();
		
		value.castReturnTypeToNarrowest(elementType);
		
		if(array.original() instanceof NewArrayOperation newArray) {
			newArray.addToInitializer(this);
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
