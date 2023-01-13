package x590.jdecompiler.operation.constant;

import x590.jdecompiler.constpool.MethodTypeConstant;
import x590.jdecompiler.type.TypeSize;

public class MethodTypeConstOperation extends LdcOperation<MethodTypeConstant> {
	
	public MethodTypeConstOperation(MethodTypeConstant value) {
		super(TypeSize.WORD, value);
	}
}
