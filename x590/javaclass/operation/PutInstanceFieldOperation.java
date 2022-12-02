package x590.javaclass.operation;

import x590.javaclass.constpool.FieldrefConstant;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;

public class PutInstanceFieldOperation extends PutFieldOperation {
	
	private final Operation object;
	
	public PutInstanceFieldOperation(DecompilationContext context, int index) {
		super(context, index);
		this.object = popObject(context);
	}

	public PutInstanceFieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		super(context, fieldref);
		this.object = popObject(context);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print(object, context).print('.');
		super.writeTo(out, context);
	}
	
	@Override
	public boolean requiresLocalContext() {
		return object.requiresLocalContext() || super.requiresLocalContext();
	}
}