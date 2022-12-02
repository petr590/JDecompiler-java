package x590.javaclass.operation;

import x590.javaclass.FieldDescriptor;
import x590.javaclass.constpool.FieldrefConstant;
import x590.javaclass.context.DecompilationContext;

public abstract class FieldOperation extends Operation {
	
	protected final FieldDescriptor descriptor;
	protected final boolean canOmit;
	
	public FieldOperation(DecompilationContext context, int index) {
		this(context, context.pool.<FieldrefConstant>get(index));
	}
	
	public FieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		this.descriptor = new FieldDescriptor(fieldref);
		this.canOmit = descriptor.clazz.equals(context.classinfo.thisType) && context.classinfo.findField(descriptor).get().isSynthetic();
	}
	
	protected Operation popObject(DecompilationContext context) {
		return context.stack.popAsNarrowest(descriptor.clazz);
	}
	
	@Override
	public boolean canOmit() {
		return canOmit;
	}
}