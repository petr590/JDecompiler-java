package x590.javaclass.operation.constant;

import x590.javaclass.ClassInfo;
import x590.javaclass.FieldDescriptor;
import x590.javaclass.JavaField;
import x590.javaclass.constpool.ConstValueConstant;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.ReturnableOperation;
import x590.javaclass.type.Type;
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