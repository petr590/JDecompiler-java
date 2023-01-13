package x590.jdecompiler.operation.constant;

import x590.jdecompiler.JavaField;
import x590.jdecompiler.constpool.IntegerConstant;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.UncertainIntegralType;
import x590.jdecompiler.util.StringUtil;

public class IConstOperation extends ConstOperation {
	
	private final int value;
	
	
	private static Type getReturnTypeFor(int value) {
		if((value & 0x1) == value)
			return PrimitiveType.BYTE_SHORT_INT_CHAR_BOOLEAN;
		
		int minCapacity =
				(byte)value == value ? 1 :
				(short)value == value ? 2 : 4;
		
		return UncertainIntegralType.getInstance(minCapacity, 4, false, (char)value == value);
	}
	
	
	public IConstOperation(int value) {
		super(getReturnTypeFor(value));
		
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	
	@Override
	public void writeValue(StringifyOutputStream out, StringifyContext context) {
		Type type = returnType.reduced();
		
		out.print(
				type.isSubtypeOf(PrimitiveType.BYTE)    ? StringUtil.toLiteral((byte)value) :
				type.isSubtypeOf(PrimitiveType.SHORT)   ? StringUtil.toLiteral((short)value) :
				type.isSubtypeOf(PrimitiveType.CHAR)    ? StringUtil.toLiteral((char)value) :
				type.isSubtypeOf(PrimitiveType.BOOLEAN) ? StringUtil.toLiteral(value != 0) :
					StringUtil.toLiteral(value)
		);
	}
	
	@Override
	public boolean isOne() {
		return value == 1;
	}
	
	@Override
	protected boolean canUseConstant(JavaField constant) {
		return super.canUseConstant(constant) && ((IntegerConstant)constant.constantValueAttribute.value).getValue() == value;
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof IConstOperation iconst && value == iconst.value && returnType.equals(iconst.returnType);
	}
}
