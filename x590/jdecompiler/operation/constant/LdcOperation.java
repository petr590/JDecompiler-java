package x590.jdecompiler.operation.constant;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.JavaField;
import x590.jdecompiler.constpool.ConstValueConstant;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.TypeSizeMismatchException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.TypeSize;

public abstract class LdcOperation<CT extends ConstValueConstant> extends ConstOperation {
	
	private final CT value;
	
	public LdcOperation(TypeSize size, CT value) {
		super(value.getType());
		
		this.value = value;
		if(value.getType().getSize() != size)
			throw new TypeSizeMismatchException(size, value.getType().getSize(), value.getType());
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		value.addImports(classinfo);
	}
	
	@Override
	public void writeValue(StringifyOutputStream out, StringifyContext context) {
		out.write(value, context.classinfo);
	}
	
	@Override
	protected boolean canUseConstant(JavaField constant) {
		return super.canUseConstant(constant) && constant.constantValueAttribute.value.equals(value);
	}
}
