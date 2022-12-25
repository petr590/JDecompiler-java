package x590.javaclass.operation.invoke;

import java.util.ArrayDeque;
import java.util.Deque;

import x590.javaclass.MethodDescriptor;
import x590.javaclass.constpool.MethodrefConstant;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.exception.DecompilationException;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.OperationWithDescriptor;
import x590.javaclass.type.Type;
import x590.javaclass.util.Util;

public abstract class InvokeOperation extends OperationWithDescriptor<MethodDescriptor> {

	protected final Deque<Operation> arguments;
	protected final boolean isStatic;
	
	private Deque<Operation> popArguments(DecompilationContext context) {
		int length = descriptor.arguments.size();
		
		Deque<Operation> arguments = new ArrayDeque<>(length);
		
		for(int i = length; i > 0; ) {
			arguments.addFirst(context.stack.popAsNarrowest(descriptor.arguments.get(--i)));
		}
		
		return arguments;
	}
	
	protected static MethodDescriptor getDescriptor(DecompilationContext context, int index) {
		return new MethodDescriptor(context.pool.<MethodrefConstant>get(index));
	}
	
	
	public InvokeOperation(DecompilationContext context, int index, boolean isStatic) {
		this(context, getDescriptor(context, index), isStatic);
	}
	
	public InvokeOperation(DecompilationContext context, MethodDescriptor descriptor, boolean isStatic) {
		super(descriptor);
		
		if(descriptor.isStaticInitializer())
			throw new DecompilationException("Cannot invoke static initializer");
		
		if(descriptor.isConstructor() && !canInvokeConstructor())
			throw new DecompilationException("Cannot invoke constructor by the " + getInstructionName() + "instruction");
		
		this.arguments = popArguments(context);
		this.isStatic = isStatic;
	}
	
	protected boolean canInvokeConstructor() {
		return false;
	}
	
	protected abstract String getInstructionName();
	
	@Override
	public Type getReturnType() {
		return descriptor.returnType;
	}
	
	protected void writeArguments(StringifyOutputStream out, StringifyContext context) {
		out.write('(');
		Util.forEachExcludingLast(arguments, arg -> out.write(arg, context), () -> out.write(", "));
		out.write(')');
	}
	
	@Override
	public boolean requiresLocalContext() {
		return arguments.stream().anyMatch(Operation::requiresLocalContext);
	}
}