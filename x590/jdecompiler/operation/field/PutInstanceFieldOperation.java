package x590.jdecompiler.operation.field;

import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;

public class PutInstanceFieldOperation extends PutFieldOperation {
	
	private final Operation object;
	
	public PutInstanceFieldOperation(DecompilationContext context, int index) {
		super(context, index);
		this.object = popObject(context);
		super.initIncData(context);
	}
	
	public PutInstanceFieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		super(context, fieldref);
		this.object = popObject(context);
		super.initIncData(context);
	}
	
	@Override
	public void writeName(StringifyOutputStream out, StringifyContext context) {
		if(!canOmitObject(context, object)) {
			out.print(object, context).print('.');
		}
		
		super.writeName(out, context);
	}
	
	@Override
	public boolean requiresLocalContext() {
		return object.requiresLocalContext() || super.requiresLocalContext();
	}
}