package x590.jdecompiler.operation.arrayload;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.AbstractOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.CastingKind;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.type.reference.ArrayType;
import x590.jdecompiler.type.reference.IArrayType;

public abstract class ArrayLoadOperation extends AbstractOperation {
	
	private final Operation array, index;
	private final Type elementType;
	
	public ArrayLoadOperation(ArrayType requiredType, DecompilationContext context) {
		this.index = context.popAsNarrowest(PrimitiveType.INT);
		this.array = context.pop();
		
		this.elementType = ((IArrayType)array.getReturnTypeAsNarrowest(requiredType)).getElementType();
	}
	
	public Operation getArray() {
		return array;
	}
	
	public Operation getIndex() {
		return index;
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.printPrioritied(this, array, context, Associativity.LEFT).print('[').print(index, context).print(']');
	}
	
	@Override
	public Type getReturnType() {
		return elementType;
	}
	
	@Override
	protected void onCastReturnType(Type type, CastingKind kind) {
		array.castReturnTypeTo(ArrayType.forType(type), kind);
	}
	
	@Override
	public boolean requiresLocalContext() {
		return array.requiresLocalContext() || index.requiresLocalContext();
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof ArrayLoadOperation operation &&
				array.equals(operation.array) && index.equals(operation.index);
	}
}
