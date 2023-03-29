package x590.jdecompiler.operation.field;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.field.FieldDescriptor;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.WrapperClassType;

public final class GetStaticFieldOperation extends GetFieldOperation {
	
	private final boolean isPrimitiveClass;
	
	private static boolean isPrimitiveClass(FieldDescriptor descriptor) {
		return descriptor.getDeclaringClass().isWrapperClassType() &&
				descriptor.getType().equals(ClassType.CLASS) &&
				descriptor.getName().equals("TYPE");
	}
	
	public GetStaticFieldOperation(DecompilationContext context, int index) {
		super(context, index);
		this.isPrimitiveClass = isPrimitiveClass(descriptor);
	}
	
	public GetStaticFieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		super(context, fieldref);
		this.isPrimitiveClass = isPrimitiveClass(descriptor);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		
		if(isPrimitiveClass) {
			out.print(((WrapperClassType)descriptor.getDeclaringClass()).getPrimitiveType(), context.getClassinfo()).print(".class");
			
		} else {
			if(!canOmitClass(context)) {
				out.print(descriptor.getDeclaringClass(), context.getClassinfo()).print('.');
			}
			
			super.writeTo(out, context);
		}
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		if(!isPrimitiveClass)
			classinfo.addImport(descriptor.getDeclaringClass());
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof GetStaticFieldOperation operation &&
				super.equals(operation);
	}
}
