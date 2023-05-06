package x590.jdecompiler.operation.invoke;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.clazz.IClassInfo;
import x590.jdecompiler.constpool.MethodrefConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.OperationWithDescriptor;
import x590.jdecompiler.operation.array.NewArrayOperation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.reference.ClassType;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.jdecompiler.util.StringUtil;
import x590.util.annotation.Nullable;

public abstract class InvokeOperation extends OperationWithDescriptor<MethodDescriptor> {
	
	private final Deque<Operation> arguments;
	
	private Deque<Operation> popArguments(DecompilationContext context) {
		List<Type> argTypes = descriptor.getArguments();
		
		Deque<Operation> arguments = new ArrayDeque<>(argTypes.size());
		
		for(int i = argTypes.size(); i > 0; ) {
			Type argType = argTypes.get(--i);
			Operation argument = context.popAsNarrowest(argType);
			
			if(argType instanceof ReferenceType referenceType && !argType.equals(ClassType.OBJECT)) {
				argument = argument.castIfNull(referenceType);
			}
			
			arguments.addFirst(argument);
		}
		
		return arguments;
	}
	
	protected static MethodDescriptor getDescriptor(DecompilationContext context, int index) {
		MethodDescriptor descriptor = context.pool.<MethodrefConstant>get(index).toDescriptor();
		
		var iclassinfo = ClassInfo.findIClassInfo(descriptor.getDeclaringClass(), context.pool);
		if(iclassinfo != null) {
			var foundMethodInfo = iclassinfo.findMethodInfo(descriptor);
			
			if(foundMethodInfo.isPresent()) {
				return foundMethodInfo.get().getGenericDescriptor();
			}
		}
		
		return descriptor;
	}
	
	public Deque<Operation> getArguments() {
		return arguments;
	}
	
	public int argumentsCount() {
		return arguments.size();
	}
	
	public boolean isArgumentsEmpty() {
		return arguments.isEmpty();
	}
	
	
	public InvokeOperation(DecompilationContext context, int index) {
		this(context, getDescriptor(context, index));
	}
	
	public InvokeOperation(DecompilationContext context, MethodDescriptor descriptor) {
		super(descriptor);
		
		if(descriptor.isStaticInitializer())
			throw new DecompilationException("Cannot invoke static initializer");
		
		if(descriptor.isConstructor() && !canInvokeConstructor())
			throw new DecompilationException("Cannot invoke constructor by the " + getInstructionName() + "instruction");
		
		this.arguments = popArguments(context);
		
		
		IClassInfo otherClassinfo = context.getClassinfo().findIClassInfo(descriptor.getDeclaringClass());
		
		if(otherClassinfo != null) {
			
			var foundMethodInfo = otherClassinfo.findMethodInfo(descriptor);
			
			if(foundMethodInfo.isPresent() && foundMethodInfo.get().getModifiers().isVarargs()) {
			
				List<Type> argTypes = descriptor.getArguments();
				
				if(!argTypes.isEmpty() && argTypes.get(argTypes.size() - 1).isArrayType()) {
					
					Operation lastOperation = arguments.getLast();
					
					if(lastOperation instanceof NewArrayOperation varargsArray && varargsArray.canInitAsList()) {
						var name = descriptor.getName();
						var argumentsCount = arguments.size() - 1 + varargsArray.getLength();
						
						if(!otherClassinfo.hasMethodByDescriptor(
								methodDescriptor ->
										!methodDescriptor.equals(descriptor) && methodDescriptor.getName().equals(name) &&
										methodDescriptor.getArguments().size() == argumentsCount)
						) {
//							varargsArray.inlineVarargs();
							
							arguments.removeLast();
							varargsArray.remove();
							
							List<Operation> initializers = varargsArray.getInitializers();
							initializers.forEach(Operation::denyImplicitCast);
							
							arguments.addAll(initializers);
						}
					}
					
				} else {
					context.warning("Varargs method " + descriptor + " must have an array as the last argument");
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
	
	private static final Pattern
			GETTER_PATTERN = Pattern.compile("get([A-Z].*)"),
			BOOLEAN_GETTER_PATTERN = Pattern.compile("is[A-Z].*"),
			PATTERN_FOR_NOT_OMIT_THIS = Pattern.compile("equals.*");
	
	@Override
	public @Nullable String getPossibleVariableName() {
		String name = descriptor.getName();
		
		var matcher = GETTER_PATTERN.matcher(name);
		if(matcher.matches()) {
			return StringUtil.toLowerCamelCase(matcher.group(1));
		}
		
		matcher = BOOLEAN_GETTER_PATTERN.matcher(name);
		if(matcher.matches()) {
			return name;
		}
		
		return null;
	}
	
	
	@Override
	protected boolean canOmitObject(StringifyContext context, Operation object) {
		// Не опускать this для вызовов методов, название которых начинается с equals
		return super.canOmitObject(context, object) && !PATTERN_FOR_NOT_OMIT_THIS.matcher(descriptor.getName()).matches();
	}
	
	
	protected void writeArguments(StringifyOutputStream out, StringifyContext context) {
		out.print('(').printAll(arguments, skipArguments(), context, ", ").print(')');
	}
	
	protected int skipArguments() {
		return 0;
	}
	
	@Override
	public boolean requiresLocalContext() {
		return arguments.stream().anyMatch(Operation::requiresLocalContext);
	}
	
	
	@Override
	public String toString() {
		return String.format("%s [ descriptor = %s, arguments = %s ]",
				this.getClass().getSimpleName(), descriptor, arguments);
	}
	
	protected boolean equals(InvokeOperation other) {
		return super.equals(other) && arguments.equals(other.arguments);
	}
}
