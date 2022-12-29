package x590.javaclass.operation.constant;

import x590.javaclass.JavaField;
import x590.javaclass.constpool.IntegerConstant;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;
import x590.javaclass.type.UncertainIntegralType;
import x590.javaclass.util.Util;

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
				type.isSubtypeOf(PrimitiveType.BYTE)    ? Util.toLiteral((byte)value) :
				type.isSubtypeOf(PrimitiveType.SHORT)   ? Util.toLiteral((short)value) :
				type.isSubtypeOf(PrimitiveType.CHAR)    ? Util.toLiteral((char)value) :
				type.isSubtypeOf(PrimitiveType.BOOLEAN) ? Util.toLiteral(value != 0) :
					Util.toLiteral(value)
		);
	}
	
	@Override
	public boolean isOne() {
		return value == 1;
	}
	
	@Override
	protected boolean canUseConstant(JavaField constant) {
		return super.canUseConstant(constant) && ((IntegerConstant)constant.constantValueAttribute.value).value == value;
	}
}