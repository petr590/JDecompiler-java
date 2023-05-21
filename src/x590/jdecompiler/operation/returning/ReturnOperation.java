package x590.jdecompiler.operation.returning;

import java.util.function.Predicate;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.AbstractOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.VoidOperation;
import x590.jdecompiler.operation.operator.TernaryOperatorOperation;
import x590.jdecompiler.scope.IfScope;
import x590.jdecompiler.type.Type;

public abstract class ReturnOperation extends AbstractOperation implements VoidOperation {
	
	private final Operation operand;
	
	
	public ReturnOperation(Type requiredType, DecompilationContext context) {
		this(methodReturnType -> methodReturnType.canCastToNarrowest(requiredType), context);
	}
	
	public ReturnOperation(Predicate<Type> predicate, DecompilationContext context) {
		
		Type methodReturnType = context.getGenericDescriptor().getReturnType();
		
		if(!predicate.test(methodReturnType))
			throw new DecompilationException("The method return type (" + methodReturnType + ")" +
					" does not match type of the `" + getInstructionName() + "` instruction");
		
		Operation operand = context.popAsNarrowest(methodReturnType);
		
		if(context.currentScope().getLastOperation() instanceof IfScope ifScope &&
				ifScope.getOperationsCount() == 1 && ifScope.getOperationAt(0) instanceof ReturnOperation returnOperation) {
			
			operand = new TernaryOperatorOperation(ifScope.getCondition(), returnOperation.operand, operand);
			ifScope.remove();
		}
		
		this.operand = operand;
		operand.allowImplicitCast();
	}
	
	
	public Operation getOperand() {
		return operand;
	}
	
	protected abstract String getInstructionName();
	
	
	@Override
	public boolean isTerminable() {
		return true;
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.printsp("return").print(operand, context);
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || this.getClass() == other.getClass() &&
				operand.equals(((ReturnOperation)other).operand);
	}
}
