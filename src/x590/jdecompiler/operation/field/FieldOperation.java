package x590.jdecompiler.operation.field;

import java.util.Optional;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.field.FieldDescriptor;
import x590.jdecompiler.field.JavaField;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.OperationWithDescriptor;

public abstract class FieldOperation extends OperationWithDescriptor<FieldDescriptor> {
	
	protected final boolean canOmit;
	
	public FieldOperation(DecompilationContext context, int index) {
		this(context, context.pool.<FieldrefConstant>get(index));
	}
	
	private boolean canOmit(ClassInfo classinfo) {
		
		if(descriptor.getDeclaringClass().equals(classinfo.getThisType())) {
			Optional<JavaField> field = classinfo.findField(descriptor);
			
			if(field.isPresent()) {
				return field.get().getModifiers().isSynthetic();
			}
		}
		
		return false;
	}
	
	public FieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		super(new FieldDescriptor(fieldref));
		
		this.canOmit = canOmit(context.getClassinfo());
	}
	
	protected Operation popObject(DecompilationContext context) {
		return context.popAsNarrowest(descriptor.getDeclaringClass()).castIfNull(descriptor.getDeclaringClass());
	}
	
	@Override
	protected boolean canOmitClass(StringifyContext context) {
		return super.canOmitClass(context) && !context.getMethodScope().hasVariableWithName(descriptor.getName());
	}
	
	@Override
	protected boolean canOmitObject(StringifyContext context, Operation object) {
		return super.canOmitObject(context, object) && !context.getMethodScope().hasVariableWithName(descriptor.getName());
	}
	
	@Override
	public boolean canOmit() {
		return canOmit;
	}
}
