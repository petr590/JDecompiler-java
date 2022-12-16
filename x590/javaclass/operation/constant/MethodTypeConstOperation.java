package x590.javaclass.operation.constant;

import x590.javaclass.constpool.MethodTypeConstant;
import x590.javaclass.type.TypeSize;

public class MethodTypeConstOperation extends LdcOperation<MethodTypeConstant> {
	
	public MethodTypeConstOperation(MethodTypeConstant value) {
		super(TypeSize.FOUR_BYTES, value);
	}
}