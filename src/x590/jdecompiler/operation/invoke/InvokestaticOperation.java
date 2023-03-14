package x590.jdecompiler.operation.invoke;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.CastOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;

public final class InvokestaticOperation extends InvokeOperation {
	
	public InvokestaticOperation(DecompilationContext context, MethodDescriptor descriptor) {
		super(context, descriptor, true);
	}
	
	
	public static Operation operationOf(DecompilationContext context, int descriptorIndex) {
		MethodDescriptor descriptor = getDescriptor(context, descriptorIndex);
		
		if(descriptor.getName().equals("valueOf")) {
			
			var clazz = descriptor.getDeclaringClass();
			var returnType = descriptor.getReturnType();
			
			if(returnType.isClassType() && returnType.equals(clazz)) {
				
				if(clazz.equals(ClassType.BYTE) && descriptor.argumentsEquals(PrimitiveType.BYTE))
					return new CastOperation(PrimitiveType.BYTE, ClassType.BYTE, true, context);
				
				if(clazz.equals(ClassType.SHORT) && descriptor.argumentsEquals(PrimitiveType.SHORT))
					return new CastOperation(PrimitiveType.SHORT, ClassType.SHORT, true, context);
				
				if(clazz.equals(ClassType.CHARACTER) && descriptor.argumentsEquals(PrimitiveType.CHAR))
					return new CastOperation(PrimitiveType.CHAR, ClassType.CHARACTER, true, context);
				
				if(clazz.equals(ClassType.INTEGER) && descriptor.argumentsEquals(PrimitiveType.INT))
					return new CastOperation(PrimitiveType.INT, ClassType.INTEGER, true, context);
				
				if(clazz.equals(ClassType.LONG) && descriptor.argumentsEquals(PrimitiveType.LONG))
					return new CastOperation(PrimitiveType.LONG, ClassType.LONG, true, context);
				
				if(clazz.equals(ClassType.FLOAT) && descriptor.argumentsEquals(PrimitiveType.FLOAT))
					return new CastOperation(PrimitiveType.FLOAT, ClassType.FLOAT, true, context);
				
				if(clazz.equals(ClassType.DOUBLE) && descriptor.argumentsEquals(PrimitiveType.DOUBLE))
					return new CastOperation(PrimitiveType.DOUBLE, ClassType.DOUBLE, true, context);
				
				if(clazz.equals(ClassType.BOOLEAN) && descriptor.argumentsEquals(PrimitiveType.BOOLEAN))
					return new CastOperation(PrimitiveType.BOOLEAN, ClassType.BOOLEAN, true, context);
			}
		}
		
		return new InvokestaticOperation(context, descriptor);
	}
	
	
	@Override
	protected String getInstructionName() {
		return "invokestatic";
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(!canOmitClass(context))
			out.print(descriptor.getDeclaringClass(), context.getClassinfo()).print('.');
		
		out.print(descriptor.getName()).printUsingFunction(this, context, InvokeOperation::writeArguments);
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		super.addImports(classinfo);
		classinfo.addImport(descriptor.getDeclaringClass());
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof InvokestaticOperation operation &&
				super.equals(operation);
	}
}
