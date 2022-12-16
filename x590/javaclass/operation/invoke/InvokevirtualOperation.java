package x590.javaclass.operation.invoke;

import x590.javaclass.MethodDescriptor;
import x590.javaclass.context.DecompilationContext;

public class InvokevirtualOperation extends InvokeNonstaticOperation {
	
	public InvokevirtualOperation(DecompilationContext context, int index) {
		super(context, index);
	}
	
	public InvokevirtualOperation(DecompilationContext context, MethodDescriptor descriptor) {
		super(context, descriptor);
	}
	
	@Override
	protected String getInstructionName() {
		return "invokevirtual";
	}
}