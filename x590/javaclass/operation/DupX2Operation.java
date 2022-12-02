package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.TypeSize;

public class DupX2Operation extends AbstractDupOperation {
	
	public DupX2Operation(DecompilationContext context) {
		super(TypeSize.FOUR_BYTES, context);
		
		Operation value1 = context.stack.pop();
		Operation value2 = context.stack.pop();
		
		if(value2.getReturnType().getSize() == TypeSize.EIGHT_BYTES) {
			context.stack.push(value1);
			context.stack.push(value2);
		} else {
			Operation value3 = context.stack.pop();
			
			context.stack.push(value1);
			context.stack.push(value3);
			context.stack.push(value2);
		}
	}
}