package x590.jdecompiler.operation.invoke;

import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.context.DecompilationContext;

public final class InvokeinterfaceOperation extends InvokeNonstaticOperation {
	
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
