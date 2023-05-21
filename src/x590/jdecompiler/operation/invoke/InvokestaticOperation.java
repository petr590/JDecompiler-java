package x590.jdecompiler.operation.invoke;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.field.FieldInfo;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.OperationUtils;
import x590.jdecompiler.operation.cast.CastOperation;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.type.reference.ClassType;
import x590.util.annotation.Nullable;

public final class InvokestaticOperation extends InvokeOperation {
	
	private @Nullable Int2ObjectMap<String> enumTable = FieldInfo.UNDEFINED_ENUM_TABLE;
	
	public InvokestaticOperation(DecompilationContext context, MethodDescriptor descriptor) {
		super(context, descriptor);
		super.initGenericDescriptor(context, null);
	}
	
	
	public static Operation operationOf(DecompilationContext context, int descriptorIndex) {
		MethodDescriptor descriptor = getDescriptor(context, descriptorIndex);
		
		if(descriptor.getName().equals("valueOf")) {
			
			var clazz = descriptor.getDeclaringClass();
			var returnType = descriptor.getReturnType();
			
			if(returnType.isClassType() && returnType.equals(clazz)) {
				
				if(clazz.equals(ClassType.BYTE) && descriptor.argumentsEquals(PrimitiveType.BYTE))
					return CastOperation.of(PrimitiveType.BYTE, ClassType.BYTE, true, context);
				
				if(clazz.equals(ClassType.SHORT) && descriptor.argumentsEquals(PrimitiveType.SHORT))
					return CastOperation.of(PrimitiveType.SHORT, ClassType.SHORT, true, context);
				
				if(clazz.equals(ClassType.CHARACTER) && descriptor.argumentsEquals(PrimitiveType.CHAR))
					return CastOperation.of(PrimitiveType.CHAR, ClassType.CHARACTER, true, context);
				
				if(clazz.equals(ClassType.INTEGER) && descriptor.argumentsEquals(PrimitiveType.INT))
					return CastOperation.of(PrimitiveType.INT, ClassType.INTEGER, true, context);
				
				if(clazz.equals(ClassType.LONG) && descriptor.argumentsEquals(PrimitiveType.LONG))
					return CastOperation.of(PrimitiveType.LONG, ClassType.LONG, true, context);
				
				if(clazz.equals(ClassType.FLOAT) && descriptor.argumentsEquals(PrimitiveType.FLOAT))
					return CastOperation.of(PrimitiveType.FLOAT, ClassType.FLOAT, true, context);
				
				if(clazz.equals(ClassType.DOUBLE) && descriptor.argumentsEquals(PrimitiveType.DOUBLE))
					return CastOperation.of(PrimitiveType.DOUBLE, ClassType.DOUBLE, true, context);
				
				if(clazz.equals(ClassType.BOOLEAN) && descriptor.argumentsEquals(PrimitiveType.BOOLEAN))
					return CastOperation.of(PrimitiveType.BOOLEAN, ClassType.BOOLEAN, true, context);
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
			out.print(getDescriptor().getDeclaringClass(), context.getClassinfo()).print('.');
		
		out.print(getDescriptor().getName()).printUsingFunction(this, context, InvokeOperation::writeArguments);
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		super.addImports(classinfo);
		classinfo.addImport(getDescriptor().getDeclaringClass());
	}
	
	
	@Override
	public @Nullable Int2ObjectMap<String> getEnumTable(DecompilationContext context) {
		return OperationUtils.initEnumTable(context, getDescriptor(), enumTable, this::setEnumTable);
	}
	
	@Override
	public void setEnumTable(Int2ObjectMap<String> enumTable) {
		this.enumTable = enumTable;
	}
	
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof InvokestaticOperation operation &&
				super.equals(operation);
	}
}
