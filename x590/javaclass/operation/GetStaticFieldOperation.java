package x590.javaclass.operation;

import x590.javaclass.ClassInfo;
import x590.javaclass.constpool.FieldrefConstant;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;

public class GetStaticFieldOperation extends GetFieldOperation {
	
	public GetStaticFieldOperation(DecompilationContext context, int index) {
		super(context, index);
	}
	
	public GetStaticFieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		super(context, fieldref);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print(descriptor.clazz.toString(context.classinfo)).print('.').print(descriptor.name);
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(descriptor.clazz);
	}
}