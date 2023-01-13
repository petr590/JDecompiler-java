package x590.jdecompiler.operation.constant;

import x590.jdecompiler.constpool.MethodHandleConstant;
import x590.jdecompiler.type.TypeSize;

public class MethodHandleConstOperation extends LdcOperation<MethodHandleConstant> {
	
	public MethodHandleConstOperation(MethodHandleConstant value) {
		super(TypeSize.WORD, value);
	}
}
