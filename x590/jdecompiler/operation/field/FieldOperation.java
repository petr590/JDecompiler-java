package x590.jdecompiler.operation.field;

import x590.jdecompiler.FieldDescriptor;
import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.OperationWithDescriptor;

public abstract class FieldOperation extends OperationWithDescriptor<FieldDescriptor> {
	
	protected final boolean canOmit;
	
	public FieldOperation(DecompilationContext context, int index) {
		this(context, context.pool.<FieldrefConstant>get(index));
	}
	
	public FieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		super(new FieldDescriptor(fieldref));
		this.canOmit = descriptor.getDeclaringClass().equals(context.classinfo.getThisType()) && context.classinfo.getField(descriptor).getModifiers().isSynthetic();
	}
	
	protected Operation popObject(DecompilationContext context) {
		return context.popAsNarrowest(descriptor.getDeclaringClass()).castIfNull(descriptor.getDeclaringClass());
	}
	
	@Override
	protected boolean canOmitClass(StringifyContext context) {
		return super.canOmitClass(context) && !context.methodScope.hasVariableWithName(descriptor.getName());
	}
	
	@Override
	protected boolean canOmitObject(StringifyContext context, Operation object) {
		return super.canOmitObject(context, object) && !context.methodScope.hasVariableWithName(descriptor.getName());
	}
	
	@Override
	public boolean canOmit() {
		return canOmit;
	}
}
