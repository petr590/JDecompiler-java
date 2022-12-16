package x590.javaclass.operation.dup;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.TypeSize;

public class Dup2Operation extends AbstractDupOperation {
	
	public Dup2Operation(DecompilationContext context) {
		super(TypeSize.EIGHT_BYTES, context);
	}
}