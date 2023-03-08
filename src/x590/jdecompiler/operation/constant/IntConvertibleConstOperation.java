package x590.jdecompiler.operation.constant;

import x590.jdecompiler.constpool.ConstableValueConstant;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;

public abstract class IntConvertibleConstOperation<C extends ConstableValueConstant<?>> extends ConstOperation<C> {
	
	protected boolean implicit;
	
	public IntConvertibleConstOperation(C constant) {
		super(constant);
	}
	
	@Override
	public Type getImplicitType() {
		return constant.canImlicitCastToInt() ? PrimitiveType.INT : returnType;
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		constant.writeTo(out, context.getClassinfo(), returnType, implicit);
	}
	
	@Override
	protected void setImplicitCast(boolean implicit) {
		this.implicit = implicit;
	}
}
