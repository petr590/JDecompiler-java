package x590.jdecompiler.operation.field;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;

public final class PutStaticFieldOperation extends PutFieldOperation {
	
	public PutStaticFieldOperation(DecompilationContext context, int index) {
		super(context, index);
		init(context);
	}
	
	public PutStaticFieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		super(context, fieldref);
		init(context);
	}
	
	private void init(DecompilationContext context) {
		if(!canOmit && context.getDescriptor().isStaticInitializer() &&
				context.currentScope() == context.getMethodScope() && !getValue().requiresLocalContext()) {
			
			if(context.getClassinfo().getField(descriptor).setStaticInitializer(getValue(), context))
				this.remove();
		}
		
		super.initIncData(context);
	}
	
	@Override
	public void writeName(StringifyOutputStream out, StringifyContext context) {
		if(!canOmitClass(context)) {
			out.print(descriptor.getDeclaringClass(), context.getClassinfo()).print('.');
		}
		
		super.writeName(out, context);
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(descriptor.getDeclaringClass());
	}
}
