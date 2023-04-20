package x590.jdecompiler.scope;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.operator.TernaryOperatorOperation;
import x590.util.annotation.Nullable;

public class ElseScope extends Scope {
	
	protected final IfScope ifScope;
	private @Nullable ElseIfPair elseIfPart;
	
	protected ElseScope(DecompilationContext context, int endIndex, IfScope ifScope) {
		super(context, endIndex, ifScope.superScope());
		this.ifScope = ifScope;
	}
	
	
	@Override
	public boolean isTerminable() {
		return this.isLastOperationTerminable() && ifScope.isLastOperationTerminable();
	}
	
	
	boolean canSelfOmitCurlyBrackets() {
		return super.canOmitCurlyBrackets() ||
				elseIfPart != null && elseIfPart.canSelfOmitCurlyBrackets();
	}
	
	boolean canSelfOmitCurlyBracketsForward() {
		return canSelfOmitCurlyBrackets() ||
				elseIfPart != null && elseIfPart.canSelfOmitCurlyBracketsForward();
	}
	
	boolean canSelfOmitCurlyBracketsBackward() {
		return canSelfOmitCurlyBrackets() && ifScope.canSelfOmitCurlyBracketsBackward();
	}
	
	@Override
	protected boolean canOmitCurlyBrackets() {
		return canSelfOmitCurlyBracketsForward() && ifScope.canSelfOmitCurlyBracketsBackward();
	}

	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(elseIfPart != null) {
			writeHeader(out, context);
			
			out.printsp();
			elseIfPart.writeTo(out, context);
			
		} else {
			super.writeTo(out, context);
		}
	}
	
	@Override
	public void writeFront(StringifyOutputStream out, StringifyContext context) {
		if(canOmitCurlyBrackets())
			out.println().printIndent();
	}
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		out.write(canOmitCurlyBrackets() ? "else" : " else");
	}
	
	@Override
	public void finalizeScope(DecompilationContext context) {
		super.finalizeScope(context);
		
		var stackState = context.getStackState(endIndex());
		
		if(ifScope.isEmpty() && this.isEmpty() && !stackState.isEmpty() && !context.stackEmpty()) {
			
			context.push(new TernaryOperatorOperation(ifScope.getCondition(), stackState.pop(), context.pop()));
			this.remove();
			ifScope.remove();
			
		} else {
			int operationsCount = getOperationsCount();
			
			if(operationsCount == 1 && getOperationAt(0) instanceof IfScope nextIfScope) {
				elseIfPart = new ElseIfPair(nextIfScope, null);
				
			} else if(operationsCount == 2 &&
					getOperationAt(0) instanceof IfScope nextIfScope &&
					getOperationAt(1) instanceof ElseScope nextElseScope) {
				
				elseIfPart = new ElseIfPair(nextIfScope, nextElseScope);
				nextIfScope.setPrevElse(this);
			}
		}
	}
	
	
	static class ElseIfPair {
		private IfScope ifScope;
		private @Nullable ElseScope elseScope;
		
		private ElseIfPair(IfScope ifScope, @Nullable ElseScope elseScope) {
			this.ifScope = ifScope;
			this.elseScope = elseScope;
		}
		
		public void writeTo(StringifyOutputStream out, StringifyContext context) {
			ifScope.writeTo(out, context);
			ifScope.writeBack(out, context);
			
			if(elseScope != null)
				elseScope.writeAsStatement(out, context);
		}
		
		public boolean canSelfOmitCurlyBrackets() {
			return ifScope.canSelfOmitCurlyBrackets() &&
					(elseScope == null || elseScope.canSelfOmitCurlyBrackets());
		}
		
		public boolean canSelfOmitCurlyBracketsForward() {
			return ifScope.canSelfOmitCurlyBracketsForward() &&
					(elseScope == null || elseScope.canSelfOmitCurlyBracketsForward());
		}
	}
}