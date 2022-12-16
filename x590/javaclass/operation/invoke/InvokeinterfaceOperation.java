package x590.javaclass.operation.invoke;

import x590.javaclass.MethodDescriptor;
import x590.javaclass.context.DecompilationContext;

public class InvokeinterfaceOperation extends InvokeNonstaticOperation {
	
	public InvokeinterfaceOperation(DecompilationContext context, int index) {
		super(context, index);
	}
	
	public InvokeinterfaceOperation(DecompilationContext context, MethodDescriptor descriptor) {
		super(context, descriptor);
	}
	
	@Override
	protected String getInstructionName() {
		return "invokeinterface";
	}
}