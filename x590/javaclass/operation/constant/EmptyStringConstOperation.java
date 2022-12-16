package x590.javaclass.operation.constant;

import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.Type;

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