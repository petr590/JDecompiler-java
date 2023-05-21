package x590.jdecompiler.operation.execstream;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.scope.Scope;

public class BreakOperation extends LabelOperation {
	
	public BreakOperation(Scope scope, boolean hasOtherScope) {
		super(scope, hasOtherScope);
	}
	
	
	@Override
	protected void writeAction(StringifyOutputStream out, StringifyContext context) {
		out.write("break");
	}


	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof BreakOperation breakOperation && this.equals(breakOperation);
	}
	
	public boolean equals(BreakOperation other) {
		return this == other ||
				getScope().equals(other.getScope()) &&
				hasOtherScope() == other.hasOtherScope();
	}
}
