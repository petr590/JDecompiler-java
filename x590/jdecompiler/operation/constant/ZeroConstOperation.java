package x590.jdecompiler.operation.constant;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;

public final class ZeroConstOperation extends Operation {
	
	public static final ZeroConstOperation INSTANCE = new ZeroConstOperation();
	
	private ZeroConstOperation() {}
	
	@Override
	public Type getReturnType() {
		return PrimitiveType.INT;
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.write('0');
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other;
	}
}
