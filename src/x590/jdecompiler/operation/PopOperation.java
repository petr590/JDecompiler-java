package x590.jdecompiler.operation;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.TypeSize;

public final class PopOperation extends VoidOperation {
	
	public final Operation value;
	
	public PopOperation(TypeSize size, DecompilationContext context) {
		this.value = context.popWithSize(size);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		value.writeTo(out, context);
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof PopOperation operation &&
				value.equals(operation.value);
	}
}
