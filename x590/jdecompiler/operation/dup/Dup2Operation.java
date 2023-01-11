package x590.jdecompiler.operation.dup;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.TypeSize;

public class Dup2Operation extends AbstractDupOperation {
	
	public Dup2Operation(DecompilationContext context) {
		super(TypeSize.EIGHT_BYTES, context);
	}
}
