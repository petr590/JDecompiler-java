package x590.jdecompiler.operation.constant;

import x590.jdecompiler.JavaField;
import x590.jdecompiler.constpool.FloatConstant;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.util.StringUtil;

public class FConstOperation extends IntConvertibleConstOperation {
	
	private final float value;
	
	public FConstOperation(float value) {
		super(PrimitiveType.FLOAT);
		this.value = value;
	}
	
	public float getValue() {
		return value;
	}
	
	
	@Override
	public void writeValue(StringifyOutputStream out, StringifyContext context) {
		out.write(implicit && (int)value == value ? StringUtil.toLiteral((int)value) : StringUtil.toLiteral(value));
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
		return super.canUseConstant(constant) && ((FloatConstant)constant.constantValueAttribute.value).getValue() == value;
	}
}
