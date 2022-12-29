package x590.javaclass.operation.constant;

import x590.javaclass.JavaField;
import x590.javaclass.constpool.DoubleConstant;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;
import x590.javaclass.util.Util;

public class DConstOperation extends IntConvertibleConstOperation {
	
	private final double value;
	
	public DConstOperation(double value) {
		super(PrimitiveType.DOUBLE);
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
	
	
	@Override
	public void writeValue(StringifyOutputStream out, StringifyContext context) {
		out.write(implicit && (int)value == value ? Util.toLiteral((int)value) : Util.toLiteral(value));
	}
	
	@Override
	public Type getImplicitType() {
		return (int)value == value ? PrimitiveType.INT : returnType;
	}
	
	@Override
	public boolean isOne() {
		return value == 1;
	}
	
	@Override
	protected boolean canUseConstant(JavaField constant) {
		return super.canUseConstant(constant) && ((DoubleConstant)constant.constantValueAttribute.value).value == value;
	}
}