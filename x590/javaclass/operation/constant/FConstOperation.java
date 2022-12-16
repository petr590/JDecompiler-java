package x590.javaclass.operation.constant;

import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;
import x590.javaclass.util.Util;

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
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
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
}