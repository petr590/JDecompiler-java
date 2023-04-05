package x590.jdecompiler.operation;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.scope.Scope;

public class ContinueOperation extends LabelOperation {
	
	public ContinueOperation(Scope scope, boolean hasOtherScope) {
		super(scope, hasOtherScope);
	}
	
	
	@Override
	protected void writeAction(StringifyOutputStream out, StringifyContext context) {
		out.write("continue");
	}


	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof ContinueOperation breakOperation && this.equals(breakOperation);
	}
	
	public boolean equals(ContinueOperation other) {
		return this == other ||
				getScope().equals(other.getScope()) &&
				hasOtherScope() == other.hasOtherScope();
	}
}
