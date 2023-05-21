package x590.jdecompiler.operation.field;

import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;

public final class GetInstanceFieldOperation extends GetFieldOperation {
	
	private final Operation object;
	
	public GetInstanceFieldOperation(DecompilationContext context, int index) {
		super(context, index);
		this.object = popObject(context);
		initGenericDescriptor(context, object);
	}
	
	public GetInstanceFieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		super(context, fieldref);
		this.object = popObject(context);
		initGenericDescriptor(context, object);
	}
	
	public Operation getObject() {
		return object;
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(!canOmitObject(context, object)) {
			out.printPrioritied(this, object, context, Associativity.LEFT).print('.');
		}
		
		super.writeName(out, context);
	}
	
	@Override
	public boolean requiresLocalContext() {
		return object.requiresLocalContext();
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof GetInstanceFieldOperation operation &&
				super.equals(operation) && object.equals(operation.object);
	}
}
