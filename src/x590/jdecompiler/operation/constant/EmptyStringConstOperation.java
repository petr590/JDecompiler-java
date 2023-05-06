package x590.jdecompiler.operation.constant;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.AbstractOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.reference.ClassType;

public final class EmptyStringConstOperation extends AbstractOperation {
	
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
