package x590.jdecompiler.operation.invoke;

import java.util.LinkedList;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.operation.CastOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;
import x590.util.annotation.Nullable;

public final class InvokevirtualOperation extends InvokeNonstaticOperation {
	
	public InvokevirtualOperation(DecompilationContext context, MethodDescriptor descriptor) {
		super(context, descriptor);
	}
	
	public InvokevirtualOperation(DecompilationContext context, MethodDescriptor descriptor, Operation object) {
		super(context, descriptor, object);
	}
	
	
	/** Несуществующий дескриптор метода, просто чтобы было */
	private static final MethodDescriptor DEFAULT_STRING_CONCAT_DESCRIPTOR =
			new MethodDescriptor(ClassType.STRING_BUILDER, "toString", ClassType.STRING);
	
	
	public static Operation operationOf(DecompilationContext context, int descriptorIndex) {
		MethodDescriptor descriptor = getDescriptor(context, descriptorIndex);
		
		var returnType = descriptor.getReturnType();
		
		if(returnType instanceof PrimitiveType primitiveType &&
				descriptor.getDeclaringClass().equals(primitiveType.getWrapperType()) &&
				descriptor.argumentsEquals()) {
			
			var name = descriptor.getName();
			
			if(returnType.equals(PrimitiveType.BYTE) && name.equals("byteValue"))
				return new CastOperation(ClassType.BYTE, PrimitiveType.BYTE, true, context);
			
			if(returnType.equals(PrimitiveType.SHORT) && name.equals("shortValue"))
				return new CastOperation(ClassType.SHORT, PrimitiveType.SHORT, true, context);
			
			if(returnType.equals(PrimitiveType.CHAR) && name.equals("charValue"))
				return new CastOperation(ClassType.CHARACTER, PrimitiveType.CHAR, true, context);
			
			if(returnType.equals(PrimitiveType.INT) && name.equals("intValue"))
				return new CastOperation(ClassType.INTEGER, PrimitiveType.INT, true, context);
			
			if(returnType.equals(PrimitiveType.LONG) && name.equals("longValue"))
				return new CastOperation(ClassType.LONG, PrimitiveType.LONG, true, context);
			
			if(returnType.equals(PrimitiveType.FLOAT) && name.equals("floatValue"))
				return new CastOperation(ClassType.FLOAT, PrimitiveType.FLOAT, true, context);
			
			if(returnType.equals(PrimitiveType.DOUBLE) && name.equals("doubleValue"))
				return new CastOperation(ClassType.DOUBLE, PrimitiveType.DOUBLE, true, context);
			
			if(returnType.equals(PrimitiveType.BOOLEAN) && name.equals("booleanValue"))
				return new CastOperation(ClassType.BOOLEAN, PrimitiveType.BOOLEAN, true, context);
			
		} else if(JDecompiler.getConfig().decompileStringBuilderAsConcatenation() &&
				descriptor.equals(ClassType.STRING_BUILDER, "toString", ClassType.STRING)) {
			
			Operation object = context.popAsNarrowest(ClassType.STRING_BUILDER);
			
			LinkedList<Operation> operands = object.getStringBuilderChain(new LinkedList<>());
			
			if(operands != null && !operands.isEmpty()) {
				return new ConcatStringsOperation(context, DEFAULT_STRING_CONCAT_DESCRIPTOR, operands);
			}
			
			return new InvokevirtualOperation(context, descriptor, object);
		}
		
		return new InvokevirtualOperation(context, descriptor);
	}
	
	@Override
	public @Nullable LinkedList<Operation> getStringBuilderChain(LinkedList<Operation> operands) {
		if(descriptor.equals(ClassType.STRING_BUILDER, "append", ClassType.STRING_BUILDER, 1)) {
			
			operands.addFirst(getArguments().getFirst());
			return object.getStringBuilderChain(operands);
		}
		
		return null;
	}
	
	@Override
	protected String getInstructionName() {
		return "invokevirtual";
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof InvokevirtualOperation operation &&
				super.equals(operation);
	}
}
