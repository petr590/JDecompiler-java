package x590.jdecompiler.operation.constant;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.Type;

public class EmptyStringConstOperation extends Operation {
	
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
}
