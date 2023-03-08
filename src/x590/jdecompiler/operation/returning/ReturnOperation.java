package x590.jdecompiler.operation.returning;

import java.util.function.Predicate;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.VoidOperation;
import x590.jdecompiler.operation.operator.TernaryOperatorOperation;
import x590.jdecompiler.scope.IfScope;
import x590.jdecompiler.type.Type;

public abstract class ReturnOperation extends VoidOperation {
	
	private final Operation operand;
	
	
	public ReturnOperation(Type requiredType, DecompilationContext context) {
		this(methodReturnType -> methodReturnType.isSubtypeOf(requiredType), context);
	}
	
	public ReturnOperation(Predicate<Type> predicate, DecompilationContext context) {
		
		Type methodReturnType = context.getDescriptor().getReturnType();
		
		if(!predicate.test(methodReturnType))
			throw new DecompilationException("The method return type (" + methodReturnType + ")" +
					" does not match type of the `" + getInstructionName() + "` instruction");
		
		Operation operand = context.popAsNarrowest(methodReturnType);
		
		if(context.currentScope().getLastOperation(context) instanceof IfScope ifScope &&
				ifScope.getOperations().size() == 1 && ifScope.getOperations().get(0) instanceof ReturnOperation returnOperation) {
			
			operand = new TernaryOperatorOperation(ifScope.getCondition(), context, operand, returnOperation.operand);
			ifScope.remove();
		}
		
		this.operand = operand;
		operand.allowImplicitCast();
	}
	
	
	protected abstract String getInstructionName();
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print("return ").print(operand, context);
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || this.getClass() == other.getClass() &&
				operand.equals(((ReturnOperation)other).operand);
	}
}
