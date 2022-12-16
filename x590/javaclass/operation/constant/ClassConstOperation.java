package x590.javaclass.operation.constant;

import x590.javaclass.constpool.ClassConstant;
import x590.javaclass.type.TypeSize;

public class ClassConstOperation extends LdcOperation<ClassConstant> {
	
	public ClassConstOperation(ClassConstant value) {
		super(TypeSize.FOUR_BYTES, value);
	}
}