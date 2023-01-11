package x590.jdecompiler.operation.field;

import x590.jdecompiler.FieldDescriptor;
import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.OperationWithDescriptor;

public abstract class FieldOperation extends OperationWithDescriptor<FieldDescriptor> {
	
	protected final boolean canOmit;
	
	public FieldOperation(DecompilationContext context, int index) {
		this(context, context.pool.<FieldrefConstant>get(index));
	}
	
	public FieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		super(new FieldDescriptor(fieldref));
		this.canOmit = descriptor.clazz.equals(context.classinfo.thisType) && context.classinfo.findField(descriptor).get().modifiers.isSynthetic();
	}
	
	protected Operation popObject(DecompilationContext context) {
		return context.stack.popAsNarrowest(descriptor.clazz);
	}
	
	@Override
	protected boolean canOmitClass(StringifyContext context) {
		return super.canOmitClass(context) && !context.methodScope.hasVariableWithName(descriptor.name);
	}
	
	@Override
	protected boolean canOmitObject(StringifyContext context, Operation object) {
		return super.canOmitObject(context, object) && !context.methodScope.hasVariableWithName(descriptor.name);
	}
	
	@Override
	public boolean canOmit() {
		return canOmit;
	}
}
