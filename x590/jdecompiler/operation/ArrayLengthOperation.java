package x590.jdecompiler.operation;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.ArrayType;

public class ArrayLengthOperation extends IntOperation {
	
	private final Operation array;
	
	public ArrayLengthOperation(DecompilationContext context) {
		this.array = context.stack.popAsNarrowest(ArrayType.ANY_ARRAY);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print(array, context).print(".length");
	}
	
	@Override
	public boolean requiresLocalContext() {
		return array.requiresLocalContext();
	}
}