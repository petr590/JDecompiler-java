package x590.javaclass.scope;

import x590.javaclass.Variable;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.AStoreOperation;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.Types;

public class SynchronizedScope extends Scope {
	
	private final Operation object;
	private final Variable variable;
	
	public SynchronizedScope(DecompilationContext context) {
		super(context, context.currentScope().endIndex());
		this.object = context.stack.popAsNarrowest(Types.ANY_OBJECT_TYPE);
		
		if(context.currentScope().getLastOperation(context) instanceof AStoreOperation astoreOperation && astoreOperation.getValue().original() == object.original()) {
			astoreOperation.remove();
			this.variable = astoreOperation.getVariable();
		} else {
			this.variable = null;
			context.warning("Cannot find variable for monitorenter instruction, maybe code is broken");
		}
	}
	
	public Variable getVariable() {
		return variable;
	}
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		out.print("synchronized(").print(object, context).print(')');
	}
}