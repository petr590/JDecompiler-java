package x590.javaclass.operation;

import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;

public class VReturnOperation extends VoidOperation {
	
	public static final VReturnOperation INSTANCE = new VReturnOperation();
	
	private VReturnOperation() {}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.write("return");
	}
}