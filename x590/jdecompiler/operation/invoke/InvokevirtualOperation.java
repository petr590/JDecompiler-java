package x590.jdecompiler.operation.invoke;

import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.operation.CastOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;

public final class InvokevirtualOperation extends InvokeNonstaticOperation {
	
	public InvokevirtualOperation(DecompilationContext context, MethodDescriptor descriptor) {
		super(context, descriptor);
	}
	
	
	public static Operation valueOf(DecompilationContext context, int index) {
		MethodDescriptor descriptor = getDescriptor(context, index);
		
		var returnType = descriptor.returnType;
		
		if(returnType.isPrimitive() && descriptor.clazz.equals(((PrimitiveType)returnType).getWrapperType()) && descriptor.argumentsEquals()) {
			
			var name = descriptor.name;
			
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
}
