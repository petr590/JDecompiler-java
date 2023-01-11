package x590.jdecompiler.operation.returning;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.VoidOperation;

public class VReturnOperation extends VoidOperation {
	
	public static final VReturnOperation INSTANCE = new VReturnOperation();
	
	private VReturnOperation() {}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.write("return");
	}
}
