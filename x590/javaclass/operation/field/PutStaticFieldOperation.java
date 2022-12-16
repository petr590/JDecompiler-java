package x590.javaclass.operation.field;

import x590.javaclass.ClassInfo;
import x590.javaclass.constpool.FieldrefConstant;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;

public class PutStaticFieldOperation extends PutFieldOperation {
	
	public PutStaticFieldOperation(DecompilationContext context, int index) {
		super(context, index);
		init(context);
	}
	
	public PutStaticFieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		super(context, fieldref);
		init(context);
	}
	
	private void init(DecompilationContext context) {
		if(!canOmit && context.descriptor.isStaticInitializer() && !value.requiresLocalContext()) {
			if(context.classinfo.getField(descriptor).setInitializer(value))
				this.remove();
		}
		
		super.initIncData(context);
	}
	
	@Override
	public void writeName(StringifyOutputStream out, StringifyContext context) {
		if(!canOmitClass(context)) {
			out.print(descriptor.clazz, context.classinfo).print('.');
		}
		
		super.writeName(out, context);
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(descriptor.clazz);
	}
}