package x590.jdecompiler.operation.invoke;

import java.util.LinkedList;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.cast.CastOperation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.type.reference.ClassType;
import x590.util.Logger;
import x590.util.annotation.Nullable;

public final class InvokevirtualOperation extends InvokeNonstaticOperation {
	
	private final Type returnType;
	
	public InvokevirtualOperation(DecompilationContext context, MethodDescriptor descriptor) {
		super(context, descriptor);
		this.returnType = getReturnType(getGenericDescriptor());
	}
	
	public InvokevirtualOperation(DecompilationContext context, MethodDescriptor descriptor, Operation object) {
		super(context, descriptor, object);
		this.returnType = getReturnType(getGenericDescriptor());
	}
	
	private Type getReturnType(MethodDescriptor descriptor) {
		return descriptor.getDeclaringClass().isArrayType() &&
				descriptor.equalsIgnoreClass(ClassType.OBJECT, "clone") ?
						descriptor.getDeclaringClass() : descriptor.getReturnType();
	}
	
	@Override
	public Type getReturnType() {
		return returnType;
	}
	
	
	/** Несуществующий дескриптор метода, просто чтобы было */
	private static final MethodDescriptor DEFAULT_STRING_CONCAT_DESCRIPTOR =
			MethodDescriptor.of(ClassType.STRING, ClassType.STRING_BUILDER, "toString");
	
	
	public static Operation operationOf(DecompilationContext context, int descriptorIndex) {
		MethodDescriptor descriptor = getDescriptor(context, descriptorIndex);
		
		var returnType = descriptor.getReturnType();
		
		if(returnType instanceof PrimitiveType primitiveType &&
				descriptor.getDeclaringClass().equals(primitiveType.getWrapperType()) &&
				descriptor.argumentsEquals()) {
			
			var name = descriptor.getName();
			
			if(returnType.equals(PrimitiveType.BYTE) && name.equals("byteValue"))
				return CastOperation.of(ClassType.BYTE, PrimitiveType.BYTE, true, context);
			
			if(returnType.equals(PrimitiveType.SHORT) && name.equals("shortValue"))
				return CastOperation.of(ClassType.SHORT, PrimitiveType.SHORT, true, context);
			
			if(returnType.equals(PrimitiveType.CHAR) && name.equals("charValue"))
				return CastOperation.of(ClassType.CHARACTER, PrimitiveType.CHAR, true, context);
			
			if(returnType.equals(PrimitiveType.INT) && name.equals("intValue"))
				return CastOperation.of(ClassType.INTEGER, PrimitiveType.INT, true, context);
			
			if(returnType.equals(PrimitiveType.LONG) && name.equals("longValue"))
				return CastOperation.of(ClassType.LONG, PrimitiveType.LONG, true, context);
			
			if(returnType.equals(PrimitiveType.FLOAT) && name.equals("floatValue"))
				return CastOperation.of(ClassType.FLOAT, PrimitiveType.FLOAT, true, context);
			
			if(returnType.equals(PrimitiveType.DOUBLE) && name.equals("doubleValue"))
				return CastOperation.of(ClassType.DOUBLE, PrimitiveType.DOUBLE, true, context);
			
			if(returnType.equals(PrimitiveType.BOOLEAN) && name.equals("booleanValue"))
				return CastOperation.of(ClassType.BOOLEAN, PrimitiveType.BOOLEAN, true, context);
			
		} else if(JDecompiler.getConfig().decompileStringBuilderAsConcatenation() &&
				descriptor.equals(ClassType.STRING, ClassType.STRING_BUILDER, "toString")) {
			
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
		if(getDescriptor().equals(ClassType.STRING_BUILDER, ClassType.STRING_BUILDER, "append", 1)) {
			
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
