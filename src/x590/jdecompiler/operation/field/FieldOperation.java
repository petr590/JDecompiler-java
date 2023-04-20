package x590.jdecompiler.operation.field;

import java.util.Optional;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.field.FieldDescriptor;
import x590.jdecompiler.field.JavaField;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.OperationWithDescriptor;
import x590.jdecompiler.type.ClassType;

public abstract class FieldOperation extends OperationWithDescriptor<FieldDescriptor> {
	
	protected final boolean canOmit;
	private boolean isEnclosingThis;
	
	public FieldOperation(DecompilationContext context, int index) {
		this(context, context.pool.<FieldrefConstant>get(index));
	}
	
	private boolean canOmit(ClassInfo classinfo) {
		
		if(!JDecompiler.getConfig().showSynthetic() && descriptor.getDeclaringClass().equals(classinfo.getThisType())) {
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
	
	private boolean isEnclosingThis(DecompilationContext context, Operation object) {
		
		if(!JDecompiler.getConfig().showSynthetic()) {
			var thisType = context.getClassinfo().getThisType();
			
			if( 	thisType.isNested() &&
					object.isThisObject(context.getMethodModifiers()) &&
					descriptor.getName().matches("this\\$\\d+") &&
					descriptor.getType() instanceof ClassType fieldType &&
					thisType.isNestmateOf(fieldType)) {
					
				var field = context.getClassinfo().findField(descriptor);
				
				if(field.isPresent() && field.get().getModifiers().isSynthetic()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	protected Operation popObject(DecompilationContext context) {
		Operation object = context.popAsNarrowest(descriptor.getDeclaringClass()).castIfNull(descriptor.getDeclaringClass());
		
		this.isEnclosingThis = isEnclosingThis(context, object);
		
		return object;
	}
	
	@Override
	protected boolean canOmitClass(StringifyContext context) {
		return  super.canOmitClass(context) &&
				!context.getMethodScope().hasVariableWithName(descriptor.getName());
	}
	
	@Override
	protected boolean canOmitObject(StringifyContext context, Operation object) {
		return  isEnclosingThis ||
				super.canOmitObject(context, object) &&
				!context.getMethodScope().hasVariableWithName(descriptor.getName());
	}
	
	@Override
	public boolean canOmit() {
		return canOmit;
	}
	
	public void writeName(StringifyOutputStream out, StringifyContext context) {
		if(isEnclosingThis) {
			out.print(descriptor.getType(), context.getClassinfo()).write(".this");
		} else {
			out.write(descriptor.getName());
		}
	}
}
