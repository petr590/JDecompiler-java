package x590.javaclass.operation.invoke;

import x590.javaclass.ClassInfo;
import x590.javaclass.MethodDescriptor;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.CastOperation;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.PrimitiveType;

public class InvokestaticOperation extends InvokeOperation {
	
	public InvokestaticOperation(DecompilationContext context, MethodDescriptor descriptor) {
		super(context, descriptor, true);
	}
	
	
	public static Operation valueOf(DecompilationContext context, int index) {
		MethodDescriptor descriptor = getDescriptor(context, index);
		
		if(descriptor.name.equals("valueOf")) {
			
			var clazz = descriptor.clazz;
			var returnType = descriptor.returnType;
			
			
			if(clazz.equals(ClassType.BYTE) && returnType.equals(ClassType.BYTE) && descriptor.argumentsEquals(PrimitiveType.BYTE))
				return new CastOperation(PrimitiveType.BYTE, ClassType.BYTE, true, context);
			
			if(clazz.equals(ClassType.SHORT) && returnType.equals(ClassType.SHORT) && descriptor.argumentsEquals(PrimitiveType.SHORT))
				return new CastOperation(PrimitiveType.SHORT, ClassType.SHORT, true, context);
			
			if(clazz.equals(ClassType.CHARACTER) && returnType.equals(ClassType.CHARACTER) && descriptor.argumentsEquals(PrimitiveType.CHAR))
				return new CastOperation(PrimitiveType.CHAR, ClassType.CHARACTER, true, context);
			
			if(clazz.equals(ClassType.INTEGER) && returnType.equals(ClassType.INTEGER) && descriptor.argumentsEquals(PrimitiveType.INT))
				return new CastOperation(PrimitiveType.INT, ClassType.INTEGER, true, context);
			
			if(clazz.equals(ClassType.LONG) && returnType.equals(ClassType.LONG) && descriptor.argumentsEquals(PrimitiveType.LONG))
				return new CastOperation(PrimitiveType.LONG, ClassType.LONG, true, context);
			
			if(clazz.equals(ClassType.FLOAT) && returnType.equals(ClassType.FLOAT) && descriptor.argumentsEquals(PrimitiveType.FLOAT))
				return new CastOperation(PrimitiveType.FLOAT, ClassType.FLOAT, true, context);
			
			if(clazz.equals(ClassType.DOUBLE) && returnType.equals(ClassType.DOUBLE) && descriptor.argumentsEquals(PrimitiveType.DOUBLE))
				return new CastOperation(PrimitiveType.DOUBLE, ClassType.DOUBLE, true, context);
			
			if(clazz.equals(ClassType.BOOLEAN) && returnType.equals(ClassType.BOOLEAN) && descriptor.argumentsEquals(PrimitiveType.BOOLEAN))
				return new CastOperation(PrimitiveType.BOOLEAN, ClassType.BOOLEAN, true, context);
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
			out.print(descriptor.clazz, context.classinfo).print('.');
		
		out.write(descriptor.name);
		writeArguments(out, context);
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		super.addImports(classinfo);
		classinfo.addImport(descriptor.clazz);
	}
}