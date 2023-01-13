package x590.jdecompiler.instruction.invoke;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.attribute.BootstrapMethodsAttribute;
import x590.jdecompiler.attribute.BootstrapMethodsAttribute.BootstrapMethod;
import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.constpool.InvokeDynamicConstant;
import x590.jdecompiler.constpool.MethodHandleConstant;
import x590.jdecompiler.constpool.MethodHandleConstant.ReferenceKind;
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
import x590.jdecompiler.type.ArrayType;
import x590.jdecompiler.type.ClassType;
import x590.util.Util;

public final class InvokedynamicInstruction extends InstructionWithIndex {
	
	public InvokedynamicInstruction(DisassemblerContext context, int index, int zeroShort) {
		super(index);
		
		if(zeroShort != 0)
			context.warning("illegal format of instruction invokedynamic at pos 0x" + Util.hex(context.currentPos()) +
					": by specification, third and fourth bytes must be zero");
	}
	
	
	private static final ClassType
			CALL_SITE = ClassType.fromDescriptor("java/lang/invoke/CallSite"),
			LOOKUP = ClassType.fromDescriptor("java/lang/invoke/MethodHandles$Lookup"),
			STRING_CONCAT_FACTORY = ClassType.fromDescriptor("java/lang/invoke/StringConcatFactory");
	
	private static final MethodDescriptor makeConcatWithConstantsDescriptor
			= new MethodDescriptor(STRING_CONCAT_FACTORY, "makeConcatWithConstants", CALL_SITE, LOOKUP, ClassType.STRING, ClassType.METHOD_TYPE, ClassType.STRING, ArrayType.OBJECT_ARRAY);
	
//	private static final MethodDescriptor publicLookupDescriptor = new MethodDescriptor(LOOKUP, "publicLookup", LOOKUP);
	
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		InvokeDynamicConstant invokeDynamicConstant = context.pool.get(index);
		BootstrapMethod bootstrapMethod =
				(context.classinfo.getAttributes().<BootstrapMethodsAttribute>get("BootstrapMethods")).bootstrapMethods.get(invokeDynamicConstant.bootstrapMethodAttrIndex);
		
		MethodHandleConstant methodHandle = bootstrapMethod.methodHandle;
		ReferenceConstant referenceConstant = methodHandle.getReferenceConstant();
		
		switch(methodHandle.referenceKind) {
			case ReferenceKind.GETFIELD: return new GetInstanceFieldOperation(context, (FieldrefConstant)referenceConstant);
			case ReferenceKind.GETSTATIC: return new GetStaticFieldOperation( context, (FieldrefConstant)referenceConstant);
			case ReferenceKind.PUTFIELD: return new PutInstanceFieldOperation(context, (FieldrefConstant)referenceConstant);
			case ReferenceKind.PUTSTATIC: return new PutStaticFieldOperation( context, (FieldrefConstant)referenceConstant);
			
			default:
				MethodDescriptor descriptor =
						new MethodDescriptor(referenceConstant.getClassConstant(), invokeDynamicConstant.getNameAndType());
				
				List<Operation> arguments = new ArrayList<>(descriptor.arguments.size());
				
				// pop arguments that already on stack
				for(int i = descriptor.arguments.size(); i > 0; i--)
					arguments.add(context.pop());
				
				
				MethodDescriptor invokedynamicDescriptor = new MethodDescriptor(referenceConstant);
				
				if(methodHandle.referenceKind == ReferenceKind.INVOKESTATIC && descriptor.name.equals("makeConcatWithConstants") &&
						invokedynamicDescriptor.equals(makeConcatWithConstantsDescriptor)) {
					
					// String concat
					if(bootstrapMethod.arguments.size() < 1)
						throw new DecompilationException("Method java.lang.invoke.StringConcatFactory.makeConcatWithConstants" +
								" must have one or more static arguments");
					
					
					IntFunction<String> getWrongTypeMessage = i ->
							"Method java.lang.invoke.StringConcatFactory.makeConcatWithConstants" +
								": wrong type of static argument #" + i +
								": expected String, got " + bootstrapMethod.arguments.get(i).getConstantName();
					
					
					if(!(bootstrapMethod.arguments.get(0) instanceof StringConstant))
						throw new DecompilationException(getWrongTypeMessage.apply(0));
					
					StringConstOperation pattern = new StringConstOperation((StringConstant)bootstrapMethod.arguments.get(0));
					
					
					int argumentsCount = bootstrapMethod.arguments.size();
					List<Operation> staticArguments = new ArrayList<>(argumentsCount - 1);
					
					for(int i = 1; i < argumentsCount; i++) {
						if(!(bootstrapMethod.arguments.get(i) instanceof StringConstant))
							throw new DecompilationException(getWrongTypeMessage.apply(i));
						
						staticArguments.add(new StringConstOperation((StringConstant)bootstrapMethod.arguments.get(i)));
					}
					
					
					// push non-static arguments on stack
					for(var iter = arguments.listIterator(arguments.size()); iter.hasPrevious();)
						context.push(iter.previous());
					
					return new ConcatStringsOperation(context, descriptor, pattern, staticArguments);
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