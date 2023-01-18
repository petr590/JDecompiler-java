package x590.jdecompiler.operation.field;

import x590.jdecompiler.constpool.FieldrefConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.io.StringifyOutputStream;

public final class PutInstanceFieldOperation extends PutFieldOperation {
	
	private final Operation object;
	
	public PutInstanceFieldOperation(DecompilationContext context, int index) {
		super(context, index);
		this.object = popObject(context);
		init(context);
	}
	
	public PutInstanceFieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		super(context, fieldref);
		this.object = popObject(context);
		init(context);
	}
	
	// TODO
	private void init(DecompilationContext context) {
		if(!canOmit && context.descriptor.isConstructor() &&
				context.currentScope() == context.methodScope && !getValue().requiresLocalContext()) {
			
			if(context.classinfo.getField(descriptor).setInstanceInitializer(getValue()))
				this.remove();
		}
		
		super.initIncData(context);
	}
	
	@Override
	public void writeName(StringifyOutputStream out, StringifyContext context) {
		if(!canOmitObject(context, object)) {
			out.printPrioritied(this, object, context, Associativity.LEFT).print('.');
		}
		
		super.writeName(out, context);
	}
	
	@Override
	public boolean requiresLocalContext() {
		return object.requiresLocalContext() || super.requiresLocalContext();
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof PutInstanceFieldOperation operation &&
				super.equals(operation) && object.equals(operation.object);
	}
}
