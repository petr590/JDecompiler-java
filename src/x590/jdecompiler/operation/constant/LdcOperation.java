package x590.jdecompiler.operation.constant;

import x590.jdecompiler.constpool.ConstValueConstant;
import x590.jdecompiler.exception.TypeSizeMismatchException;
import x590.jdecompiler.type.TypeSize;

public abstract class LdcOperation<CT extends ConstValueConstant> extends ConstOperation<CT> {
	
	public LdcOperation(TypeSize size, CT constant) {
		super(constant);
		
		if(constant.getType().getSize() != size)
			throw new TypeSizeMismatchException(size, constant.getType().getSize(), constant.getType());
	}
}
