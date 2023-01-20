package x590.jdecompiler.operation.invoke;

import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.CastOperation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;

public final class InvokevirtualOperation extends InvokeNonstaticOperation {
	
	public InvokevirtualOperation(DecompilationContext context, MethodDescriptor descriptor) {
		super(context, descriptor);
	}
	
	
	public static Operation operationOf(DecompilationContext context, int descriptorIndex) {
		MethodDescriptor descriptor = getDescriptor(context, descriptorIndex);
		
		var returnType = descriptor.getReturnType();
		
		if(returnType.isPrimitive() && descriptor.getDeclaringClass().equals(((PrimitiveType)returnType).getWrapperType()) && descriptor.argumentsEquals()) {
			
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
		}
		
		return new InvokevirtualOperation(context, descriptor);
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
