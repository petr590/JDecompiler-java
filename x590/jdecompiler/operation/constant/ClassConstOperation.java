package x590.jdecompiler.operation.constant;

import x590.jdecompiler.constpool.ClassConstant;
import x590.jdecompiler.type.TypeSize;

public class ClassConstOperation extends LdcOperation<ClassConstant> {
	
	public ClassConstOperation(ClassConstant value) {
		super(TypeSize.FOUR_BYTES, value);
	}
}
