package x590.javaclass.operation.dup;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.TypeSize;

public class DupX1Operation extends AbstractDupOperation {
	
	public DupX1Operation(DecompilationContext context) {
		super(TypeSize.FOUR_BYTES, context);
		
		Operation value1 = context.stack.popWithSize(TypeSize.FOUR_BYTES);
		Operation value2 = context.stack.popWithSize(TypeSize.FOUR_BYTES);
		
		context.stack.push(value1);
		context.stack.push(value2);
	}
}