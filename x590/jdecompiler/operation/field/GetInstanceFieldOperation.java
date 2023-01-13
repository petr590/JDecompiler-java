package x590.jdecompiler.operation.field;

import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;

public class GetInstanceFieldOperation extends GetFieldOperation {
	
	private final Operation object;
	
	public GetInstanceFieldOperation(DecompilationContext context, int index) {
		super(context, index);
		this.object = popObject(context);
	}
	
	public GetInstanceFieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		super(context, fieldref);
		this.object = popObject(context);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(!canOmitObject(context, object)) {
			out.printPrioritied(this, object, context, Associativity.LEFT).print('.');
		}
		
		super.writeTo(out, context);
	}
	
	@Override
	public boolean requiresLocalContext() {
		return object.requiresLocalContext();
	}
}
