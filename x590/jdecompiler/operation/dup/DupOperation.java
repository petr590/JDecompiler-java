package x590.jdecompiler.operation.dup;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.type.TypeSize;

public class DupOperation extends AbstractDupOperation {
	
	public DupOperation(DecompilationContext context) {
		super(TypeSize.FOUR_BYTES, context);
	}
}
