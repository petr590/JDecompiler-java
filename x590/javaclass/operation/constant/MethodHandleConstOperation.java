package x590.javaclass.operation.constant;

import x590.javaclass.constpool.MethodHandleConstant;
import x590.javaclass.type.TypeSize;

public class MethodHandleConstOperation extends LdcOperation<MethodHandleConstant> {
	
	public MethodHandleConstOperation(MethodHandleConstant value) {
		super(TypeSize.FOUR_BYTES, value);
	}
}