package x590.javaclass.operation;

import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.Type;
import x590.javaclass.type.Types;

public class AConstNullOperation extends Operation {
	
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
}