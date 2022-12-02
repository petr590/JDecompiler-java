package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.TypeSize;

public class Dup2X1Operation extends AbstractDupOperation {
	
	public Dup2X1Operation(DecompilationContext context) {
		super(TypeSize.EIGHT_BYTES, context);
		
		Operation value1 = context.stack.popWithSize(TypeSize.EIGHT_BYTES);
		Operation value2 = context.stack.popWithSize(TypeSize.FOUR_BYTES);
		
		context.stack.push(value1);
		context.stack.push(value2);
	}
}