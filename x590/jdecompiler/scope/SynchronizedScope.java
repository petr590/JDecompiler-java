package x590.jdecompiler.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.store.AStoreOperation;
import x590.jdecompiler.type.Types;
import x590.jdecompiler.variable.EmptyableVariable;

public class SynchronizedScope extends Scope {
	
	private final Operation object;
	private final EmptyableVariable variable;
	
	public SynchronizedScope(DecompilationContext context) {
		super(context, context.currentScope().endIndex());
		this.object = context.popAsNarrowest(Types.ANY_OBJECT_TYPE);
		
		if(context.currentScope().getLastOperation(context) instanceof AStoreOperation astoreOperation && astoreOperation.getValue() == object) {
			astoreOperation.remove();
			this.variable = astoreOperation.getVariable();
		} else {
			this.variable = null;
			context.warning("Cannot find variable for monitorenter instruction, maybe code is broken");
		}
	}
	
	public EmptyableVariable getVariable() {
		return variable;
	}
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		out.print("synchronized(").print(object, context).print(')');
	}
}