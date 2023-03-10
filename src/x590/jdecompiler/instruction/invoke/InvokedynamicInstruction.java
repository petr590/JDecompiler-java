package x590.jdecompiler.instruction.invoke;

import java.util.ArrayList;
import java.util.List;

import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.attribute.BootstrapMethodsAttribute.BootstrapMethod;
import x590.jdecompiler.constpool.InvokeDynamicConstant;
import x590.jdecompiler.constpool.MethodHandleConstant;
import x590.jdecompiler.constpool.MethodHandleConstant.ReferenceKind;
import x590.jdecompiler.constpool.MethodTypeConstant;
import x590.jdecompiler.constpool.ReferenceConstant;
import x590.jdecompiler.constpool.StringConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.DisassemblerContext;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.instruction.InstructionWithIndex;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.constant.StringConstOperation;
import x590.jdecompiler.operation.field.GetInstanceFieldOperation;
import x590.jdecompiler.operation.field.GetStaticFieldOperation;
import x590.jdecompiler.operation.field.PutInstanceFieldOperation;
import x590.jdecompiler.operation.field.PutStaticFieldOperation;
import x590.jdecompiler.operation.invoke.ConcatStringsOperation;
import x590.jdecompiler.operation.invoke.LambdaOperation;
import x590.jdecompiler.type.ArrayType;
import x590.jdecompiler.type.ClassType;
import x590.util.IntegerUtil;

public final class InvokedynamicInstruction extends InstructionWithIndex {
	
	public InvokedynamicInstruction(DisassemblerContext context, int index, int zeroShort) {
		super(index);
		
		if(zeroShort != 0)
			context.warning("illegal format of instruction invokedynamic at pos 0x" + IntegerUtil.hex(context.currentPos()) +
					": by specification, third and fourth bytes must be zero");
	}
	
	
	private static final ClassType
			CALL_SITE = ClassType.fromDescriptor("java/lang/invoke/CallSite"),
			LOOKUP = ClassType.fromDescriptor("java/lang/invoke/MethodHandles$Lookup"),
			STRING_CONCAT_FACTORY = ClassType.fromDescriptor("java/lang/invoke/StringConcatFactory"),
			LAMBDA_METAFACTORY = ClassType.fromDescriptor("java/lang/invoke/LambdaMetafactory");
	
	private static final MethodDescriptor
			MAKE_CONCAT_WITH_CONSTANTS_DESCRIPTOR = new MethodDescriptor(STRING_CONCAT_FACTORY, "makeConcatWithConstants", CALL_SITE, LOOKUP, ClassType.STRING, ClassType.METHOD_TYPE, ClassType.STRING, ArrayType.OBJECT_ARRAY),
			LAMBDA_METAFACTORY_DESCRIPTOR = new MethodDescriptor(LAMBDA_METAFACTORY, "metafactory", CALL_SITE, LOOKUP, ClassType.STRING, ClassType.METHOD_TYPE, ClassType.METHOD_TYPE, ClassType.METHOD_HANDLE, ClassType.METHOD_TYPE);
	
//	private static final MethodDescriptor publicLookupDescriptor = new MethodDescriptor(LOOKUP, "publicLookup", LOOKUP);
	
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		InvokeDynamicConstant invokeDynamicConstant = context.pool.get(index);
		BootstrapMethod bootstrapMethod = invokeDynamicConstant.getBootstrapMethod(context.getClassinfo().getAttributes());
		
		MethodHandleConstant methodHandle = bootstrapMethod.getMethodHandle();
		
		switch(methodHandle.getReferenceKind()) {
			case GETFIELD: return new GetInstanceFieldOperation(context, methodHandle.getFieldrefConstant());
			case GETSTATIC: return new GetStaticFieldOperation( context, methodHandle.getFieldrefConstant());
			case PUTFIELD: return new PutInstanceFieldOperation(context, methodHandle.getFieldrefConstant());
			case PUTSTATIC: return new PutStaticFieldOperation( context, methodHandle.getFieldrefConstant());
			
			default: {
				ReferenceConstant referenceConstant = methodHandle.getReferenceConstant();
				
				MethodDescriptor descriptor =
						new MethodDescriptor(referenceConstant.getClassConstant(), invokeDynamicConstant.getNameAndType());
				
				List<Operation> arguments = new ArrayList<>(descriptor.getArgumentsCount());
				
				// pop arguments that already on stack
				for(int i = descriptor.getArgumentsCount(); i > 0; i--)
					arguments.add(context.pop());
				
				
				MethodDescriptor invokedynamicDescriptor = new MethodDescriptor(referenceConstant);
				
				if(methodHandle.getReferenceKind() == ReferenceKind.INVOKESTATIC) {
					
					if(descriptor.getName().equals("makeConcatWithConstants") && invokedynamicDescriptor.equals(MAKE_CONCAT_WITH_CONSTANTS_DESCRIPTOR)) {
						
						// String concat
						if(bootstrapMethod.getArgumentsCount() < 1)
							throw new DecompilationException("Method java.lang.invoke.StringConcatFactory.makeConcatWithConstants" +
									" must have one or more static arguments");
						
						StringConstOperation pattern = new StringConstOperation(bootstrapMethod.getArgument(StringConstant.class, 0));
						
						
						int argumentsCount = bootstrapMethod.getArgumentsCount();
						List<Operation> staticArguments = new ArrayList<>(argumentsCount - 1);
						
						for(int i = 1; i < argumentsCount; i++) {
							staticArguments.add(new StringConstOperation(bootstrapMethod.getArgument(StringConstant.class, i)));
						}
						
						
						// push non-static arguments on stack
						for(var iter = arguments.listIterator(arguments.size()); iter.hasPrevious();)
							context.push(iter.previous());
						
						return new ConcatStringsOperation(context, descriptor, pattern, staticArguments);
					}
					
					
					if(invokedynamicDescriptor.equals(LAMBDA_METAFACTORY_DESCRIPTOR)) {
						
						if(bootstrapMethod.getArgumentsCount() != 3)
							throw new DecompilationException("Method java.lang.invoke.LambdaMetafactory.metafactory" +
									" must have three static arguments");
						
						return new LambdaOperation(context, arguments,
								bootstrapMethod.getArgument(MethodHandleConstant.class, 1).getMethodrefConstant(),
								bootstrapMethod.getArgument(MethodTypeConstant.class, 2));
					}
				}
				
				
				throw new DecompilationException("invokedynamic instruction is not recognized: " +
						"descriptor = " + descriptor + ", invokedynamicDescriptor = " + invokedynamicDescriptor);
				
				
//				// push lookup argument
//				context.stack.push(new InvokestaticOperation(context, publicLookupDescriptor));
//				
//				context.stack.push(new StringConstOperation(invokeDynamicConstant.getNameAndType().getNameConstant().getString())); // name argument
//				
//				context.stack.push(new MethodTypeConstOperation(
//						new MethodTypeConstant(invokeDynamicConstant.getNameAndType().getDescriptor()))); // type argument
//				
//				// push static arguments on stack
//				for(int i = 0, argumentsCount = bootstrapMethod.arguments.size(); i < argumentsCount; i++)
//					context.stack.push(bootstrapMethod.arguments.get(i).toOperation());
//				
//				// push non-static arguments on stack
//				for(Operation operation : arguments)
//					context.stack.push(operation);
//				
//				switch(methodHandle.referenceKind) {
//					case ReferenceKind.INVOKEVIRTUAL: return new InvokevirtualOperation(context, invokedynamicDescriptor);
//					case ReferenceKind.INVOKESTATIC:  return new InvokestaticOperation(context, invokedynamicDescriptor);
//					case ReferenceKind.INVOKESPECIAL: return new InvokespecialOperation(context, invokedynamicDescriptor);
//					case ReferenceKind.NEWINVOKESPECIAL: return new InvokespecialOperation(context, invokedynamicDescriptor,
//							new NewOperation(referenceConstant.getClassConstant().toClassType()));
//					case ReferenceKind.INVOKEINTERFACE: return new InvokeinterfaceOperation(context, descriptor);
//					default: throw new DecompilationException("Illegal referenceKind " + methodHandle.referenceKind);
//				}
			}
		}
	}
}