package x590.jdecompiler.operation.array;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.IntOperation;
import x590.jdecompiler.type.ArrayType;

public final class ArrayLengthOperation extends IntOperation {
	
	private final Operation array;
	
	public ArrayLengthOperation(DecompilationContext context) {
		this.array = context.popAsNarrowest(ArrayType.ANY_ARRAY);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print(array, context).print(".length");
	}
	
	@Override
	public boolean requiresLocalContext() {
		return array.requiresLocalContext();
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof ArrayLengthOperation operation &&
				array.equals(operation.array);
	}
}