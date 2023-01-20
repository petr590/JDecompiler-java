package x590.jdecompiler.operation.invoke;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.OperationWithDescriptor;
import x590.jdecompiler.operation.array.NewArrayOperation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.ReferenceType;
import x590.jdecompiler.type.Type;
import x590.util.Util;

public abstract class InvokeOperation extends OperationWithDescriptor<MethodDescriptor> {
	
	protected final Deque<Operation> arguments;
	protected final boolean isStatic;
	
	private Deque<Operation> popArguments(DecompilationContext context) {
		List<Type> argTypes = descriptor.getArguments();
		
		Deque<Operation> arguments = new ArrayDeque<>(argTypes.size());
		
		for(int i = argTypes.size(); i > 0; ) {
			Type argType = argTypes.get(--i);
			Operation argument = context.popAsNarrowest(argType);
			
			if(argType.isBasicReferenceType() && !argType.equals(ClassType.OBJECT)) {
				argument = argument.castIfNull((ReferenceType)argType);
			}
			
			arguments.addFirst(argument);
		}
		
		return arguments;
	}
	
	protected static MethodDescriptor getDescriptor(DecompilationContext context, int index) {
		return new MethodDescriptor(context.pool.get(index));
	}
	
	public Deque<Operation> getArguments() {
		return arguments;
	}
	
	public int argumentsCount() {
		return arguments.size();
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
		
		
		List<Type> argTypes = descriptor.getArguments();
		
		if(!argTypes.isEmpty() && argTypes.get(argTypes.size() - 1).isBasicArrayType()) {
			
			ClassInfo otherClassinfo = ClassInfo.findClassInfo(descriptor.getDeclaringClass());
			
			Operation lastOperation = arguments.getLast();
			
			if(lastOperation instanceof NewArrayOperation varargsArray && varargsArray.canInitAsList()) {
				var name = descriptor.getName();
				var argumentsCount = arguments.size() - 1 + varargsArray.getLength();
				
				if(otherClassinfo != null && !otherClassinfo.hasMethodByDescriptor(
						methodDescriptor -> methodDescriptor != descriptor && methodDescriptor.getName().equals(name) && methodDescriptor.getArguments().size() == argumentsCount)) {
					
					arguments.getLast().inlineVarargs();
				}
			}
		}
	}
	
	protected boolean canInvokeConstructor() {
		return false;
	}
	
	protected abstract String getInstructionName();
	
	@Override
	public Type getReturnType() {
		return descriptor.getReturnType();
	}
	
	
	private static final Pattern METHOD_NAME_PATTERN = Pattern.compile("(is|get|set|equals).*");
	
	@Override
	protected boolean canOmitObject(StringifyContext context, Operation object) {
		
		// Не опускать this для вызовов методов, название которых начинается с is, get, set, equals
		return super.canOmitObject(context, object) && !METHOD_NAME_PATTERN.matcher(descriptor.getName()).matches();
	}
	
	
	protected void writeArguments(StringifyOutputStream out, StringifyContext context) {
		out.write('(');
		Util.forEachExcludingLast(arguments, arg -> out.write(arg, context), arg -> out.write(", "), skipArguments());
		out.write(')');
	}
	
	protected int skipArguments() {
		return 0;
	}
	
	@Override
	public boolean requiresLocalContext() {
		return arguments.stream().anyMatch(Operation::requiresLocalContext);
	}
	
	protected boolean equals(InvokeOperation other) {
		return super.equals(other) && arguments.equals(other.arguments);
	}
}
