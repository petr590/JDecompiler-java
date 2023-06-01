package x590.jdecompiler.scope;

import java.util.Collections;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.load.ALoadOperation;
import x590.jdecompiler.operation.other.AThrowOperation;

public class FinallyScope extends CatchScope {
	
	public FinallyScope(DecompilationContext context, int endIndex, boolean hasNext) {
		super(context, endIndex, Collections.emptyList(), hasNext);
	}
	
	@Override
	public void addOperation(Operation operation, int fromIndex) {
		// Не добавлять {@code throw ex;} в конец finally
		
		if(operation instanceof AThrowOperation athrow &&
			athrow.getOperand() instanceof ALoadOperation aload &&
			aload.getVariable() == getLoadOperation().getVariable()) {
			
			setEndIndex(fromIndex);
			
		} else {
			super.addOperation(operation, fromIndex);
		}
	}
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		out.write("finally");
	}
}
