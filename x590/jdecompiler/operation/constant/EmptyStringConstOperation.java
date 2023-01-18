package x590.jdecompiler.operation.constant;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.Type;

public final class EmptyStringConstOperation extends Operation {
	
	public static final EmptyStringConstOperation INSTANCE = new EmptyStringConstOperation();
	
	private EmptyStringConstOperation() {}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.write("\"\"");
	}
	
	@Override
	public Type getReturnType() {
		return ClassType.STRING;
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other;
	}
}
