package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.load.LoadOperation;
import x590.javaclass.type.PrimitiveType;

public class IIncOperation extends OperationWithVariable {
	
	private final int value;
	private boolean isPreInc;
	
	public IIncOperation(DecompilationContext context, int index, int value) {
		super(context.currentScope().getDefinedVariable(index));
		
		this.value = value;
		
		variable.castTypeToWidest(PrimitiveType.INT);
		
		if(!context.stack.empty() && tryLoadSameVariable(context.stack.peek())) {
			context.stack.pop();
			
		} else if(value == 1 || value == -1) {
			context.stack.onNextPush(operation -> {
				if(tryLoadSameVariable(operation)) {
					isPreInc = true;
					context.stack.push(this);
					context.currentScope().remove(this);
					return false;
				}
				
				return true;
			});
		}
	}
	
	private boolean tryLoadSameVariable(Operation operation) {
		if(operation.original() instanceof LoadOperation loadOperation && loadOperation.getVariable() == variable) {
			returnType = variable.getType();
			return true;
		}
		
		return false;
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(isPreInc) {
			out.write(value > 0 ? "++" : "--");
		}
		
		out.write(variable.getName());
		
		if(!isPreInc) {
			if(value < 0)
				out.write(value == -1 ? "--" : " -= " + value);
			else
				out.write(value == 1 ? "++" : " += " + value);
		}
	}
	
	@Override
	public int getPriority() {
		return Priority.POST_INCREMENT;
	}
}
