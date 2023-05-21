package x590.jdecompiler.operation.field;

import java.util.Optional;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.clazz.IClassInfo;
import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.field.FieldDescriptor;
import x590.jdecompiler.field.FieldInfo;
import x590.jdecompiler.field.JavaField;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.OperationWithDescriptor;
import x590.jdecompiler.type.reference.ClassType;

public abstract class FieldOperation extends OperationWithDescriptor<FieldDescriptor> {
	
	protected final boolean canOmit;
	private boolean isEnclosingThis;
	
	public FieldOperation(DecompilationContext context, int index) {
		this(context, context.pool.<FieldrefConstant>get(index));
	}
	
	public FieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		super(FieldDescriptor.from(fieldref));
		
		this.canOmit = canOmit(context.getClassinfo());
	}
	
	private boolean canOmit(ClassInfo classinfo) {
		
		if(!JDecompiler.getConfig().showSynthetic() && getDescriptor().getDeclaringClass().equals(classinfo.getThisType())) {
			Optional<JavaField> field = classinfo.findField(getDescriptor());
			
			if(field.isPresent()) {
				return field.get().getModifiers().isSynthetic();
			}
		}
		
		return false;
	}
	
	private boolean isEnclosingThis(DecompilationContext context, Operation object) {
		
		if(!JDecompiler.getConfig().showSynthetic()) {
			var thisType = context.getClassinfo().getThisType();
			
			if( 	thisType.isNested() &&
					object.isThisObject(context.getMethodModifiers()) &&
					getDescriptor().getName().matches("this\\$\\d+") &&
					getDescriptor().getType() instanceof ClassType fieldType &&
					thisType.isNestmateOf(fieldType)) {
					
				var field = context.getClassinfo().findField(getDescriptor());
				
				if(field.isPresent() && field.get().getModifiers().isSynthetic()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	protected Operation popObject(DecompilationContext context) {
		Operation object = context.popAsNarrowest(getDescriptor().getDeclaringClass()).castIfNecessary(getDescriptor().getDeclaringClass());
		
		this.isEnclosingThis = isEnclosingThis(context, object);
		
		return object;
	}
	
	@Override
	protected Optional<? extends FieldInfo> findMemberInfo(IClassInfo classinfo, FieldDescriptor descriptor) {
		return classinfo.findFieldInfoInThisAndSuperClasses(descriptor);
	}
	
	
	@Override
	protected boolean canOmitClass(StringifyContext context) {
		return  super.canOmitClass(context) &&
				!context.getMethodScope().hasVariableWithName(getDescriptor().getName());
	}
	
	@Override
	protected boolean canOmitObject(StringifyContext context, Operation object) {
		return  isEnclosingThis ||
				super.canOmitObject(context, object) &&
				!context.getMethodScope().hasVariableWithName(getDescriptor().getName());
	}
	
	@Override
	public boolean canOmit() {
		return canOmit;
	}
	
	public void writeName(StringifyOutputStream out, StringifyContext context) {
		if(isEnclosingThis) {
			out.print(getDescriptor().getType(), context.getClassinfo()).write(".this");
		} else {
			out.write(getDescriptor().getName());
		}
	}
}
