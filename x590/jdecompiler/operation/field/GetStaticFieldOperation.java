package x590.jdecompiler.operation.field;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.FieldDescriptor;
import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.WrapperClassType;

public class GetStaticFieldOperation extends GetFieldOperation {
	
	private final boolean isPrimitiveClass;
	
	private static boolean isPrimitiveClass(FieldDescriptor descriptor) {
		return descriptor.clazz.isWrapperClassType() &&
				descriptor.type.equals(ClassType.CLASS) &&
				descriptor.name.equals("TYPE");
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
			out.print(((WrapperClassType)descriptor.clazz).getPrimitiveType(), context.classinfo).print(".class");
			
		} else {
			if(!canOmitClass(context)) {
				out.print(descriptor.clazz, context.classinfo).print('.');
			}
			
			super.writeTo(out, context);
		}
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		if(!isPrimitiveClass)
			classinfo.addImport(descriptor.clazz);
	}
}
