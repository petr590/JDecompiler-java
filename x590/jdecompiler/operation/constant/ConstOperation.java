package x590.jdecompiler.operation.constant;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.FieldDescriptor;
import x590.jdecompiler.JavaField;
import x590.jdecompiler.constpool.ConstValueConstant;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.ReturnableOperation;
import x590.jdecompiler.type.Type;
import x590.util.annotation.RemoveIfNotUsed;
import x590.util.lazyloading.FunctionalLazyLoadingValue;

public abstract class ConstOperation extends ReturnableOperation {
	
	@RemoveIfNotUsed
	private FieldDescriptor ownerConstant;
	
	private final FunctionalLazyLoadingValue<ClassInfo, JavaField> constantLoader = ConstValueConstant.getConstantLoader(constant -> canUseConstant(constant));
	
	public ConstOperation(Type returnType) {
		super(returnType);
	}
	
	@Override
	public final void setOwnerConstant(FieldDescriptor ownerConstant) {
		this.ownerConstant = ownerConstant;
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		if(constantLoader.isNonNull(classinfo))
			constantLoader.get().addImports(classinfo);
	}
	
	@Override
	public final void writeTo(StringifyOutputStream out, StringifyContext context) {
		
		if(constantLoader.isNonNull(context.classinfo)) {
			JavaField constant = constantLoader.get();
			
			if(!context.classinfo.canOmitClass(constant.descriptor))
				out.print(constant.descriptor.clazz, context.classinfo).print('.');
			
			out.write(constant.descriptor.name);
			
		} else {
			this.writeValue(out, context);
		}
	}
	
	public abstract void writeValue(StringifyOutputStream out, StringifyContext context);
	
	protected boolean canUseConstant(JavaField constant) {
		return constant.descriptor.type.isSubtypeOf(returnType);
	}
}
