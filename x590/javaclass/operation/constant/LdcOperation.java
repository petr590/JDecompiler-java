package x590.javaclass.operation.constant;

import x590.javaclass.ClassInfo;
import x590.javaclass.JavaField;
import x590.javaclass.constpool.ConstValueConstant;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.exception.TypeSizeMismatchException;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.TypeSize;

public abstract class LdcOperation<CT extends ConstValueConstant> extends ConstOperation {
	
	private final CT value;
	
	public LdcOperation(TypeSize size, CT value) {
		super(value.getType());
		
		this.value = value;
		if(value.getType().getSize() != size)
			throw new TypeSizeMismatchException(size, value.getType().getSize(), value.getType());
	}
	
	
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