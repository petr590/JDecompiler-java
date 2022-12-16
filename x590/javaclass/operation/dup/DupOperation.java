package x590.javaclass.operation.dup;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.TypeSize;

public class DupOperation extends AbstractDupOperation {
	
	public DupOperation(DecompilationContext context) {
		super(TypeSize.FOUR_BYTES, context);
	}
}