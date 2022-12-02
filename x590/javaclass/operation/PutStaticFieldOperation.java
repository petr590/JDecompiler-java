package x590.javaclass.operation;

import x590.javaclass.ClassInfo;
import x590.javaclass.constpool.FieldrefConstant;
import x590.javaclass.context.DecompilationContext;

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
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(descriptor.clazz);
	}
}