package x590.javaclass.operation.constant;

import x590.javaclass.ClassInfo;
import x590.javaclass.constpool.ConstValueConstant;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.exception.TypeSizeMismatchException;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.Type;
import x590.javaclass.type.TypeSize;

public class LdcOperation<CT extends ConstValueConstant> extends Operation {
	
	private final CT value;
	
	public LdcOperation(TypeSize size, CT value) {
		this.value = value;
		if(value.getType().getSize() != size)
			throw new TypeSizeMismatchException(size, value.getType().getSize(), value.getType());
	}
	
	
	public void addImports(ClassInfo classinfo) {
		value.addImports(classinfo);
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.write(value, context.classinfo);
	}
	
	@Override
	public Type getReturnType() {
		return value.getType();
	}
}