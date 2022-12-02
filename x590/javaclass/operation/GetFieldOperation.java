package x590.javaclass.operation;

import x590.javaclass.constpool.FieldrefConstant;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.Type;

public abstract class GetFieldOperation extends FieldOperation {
	
	public GetFieldOperation(DecompilationContext context, int index) {
		super(context, index);
	}
	
	public GetFieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		super(context, fieldref);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.write(descriptor.name);
	}
	
	@Override
	public Type getReturnType() {
		return descriptor.type;
	}
}