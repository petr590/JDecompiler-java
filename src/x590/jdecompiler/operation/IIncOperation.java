package x590.jdecompiler.operation;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.load.LoadOperation;
import x590.jdecompiler.type.PrimitiveType;

public final class IIncOperation extends OperationWithVariable {
	
	private final int value;
	private boolean isPreInc;
	
	public IIncOperation(DecompilationContext context, int index, int value) {
		super(context.currentScope().getDefinedVariable(index));
		
		this.value = value;
		
		variable.castTypeToWidest(PrimitiveType.INT);
		
		if(!context.hasBreakAtCurrentIndex()) {
			
			if(!context.stackEmpty() && tryLoadSameVariable(context.peek())) {
				context.pop();
				
			} else if(value == 1 || value == -1) {
				context.onNextPush(operation -> {
					if(tryLoadSameVariable(operation)) {
						isPreInc = true;
						context.push(this);
						context.currentScope().remove(this);
						return false;
					}
					
					return true;
				});
			}
		}
	}
	
	private boolean tryLoadSameVariable(Operation operation) {
		if(operation instanceof LoadOperation loadOperation && loadOperation.getVariable().equals(variable)) {
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
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof IIncOperation operation &&
				super.equals(operation) && value == operation.value;
	}
}