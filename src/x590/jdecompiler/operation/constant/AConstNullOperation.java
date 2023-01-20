package x590.jdecompiler.operation.constant;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.CastOperation;
import x590.jdecompiler.type.ReferenceType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.Types;

public final class AConstNullOperation extends Operation {
	
	public static final AConstNullOperation INSTANCE = new AConstNullOperation();
	
	private AConstNullOperation() {}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext conetxt) {
		out.write("null");
	}
	
	@Override
	public Type getReturnType() {
		return Types.ANY_OBJECT_TYPE;
	}
	
	@Override
	public Operation castIfNull(ReferenceType clazz) {
		return new CastOperation(Types.ANY_OBJECT_TYPE, clazz, false, this);
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other;
	}
}
