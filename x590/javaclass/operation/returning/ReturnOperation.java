package x590.javaclass.operation.returning;

import java.util.function.Predicate;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.exception.DecompilationException;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.VoidOperation;
import x590.javaclass.type.Type;

public abstract class ReturnOperation extends VoidOperation {
	
	private final Operation operand;
	
	
	public ReturnOperation(Type requiredType, DecompilationContext context) {
		this(methodReturnType -> methodReturnType.isSubtypeOf(requiredType), context);
	}
	
	public ReturnOperation(Predicate<Type> predicate, DecompilationContext context) {
		
		Type methodReturnType = context.descriptor.returnType;
		
		if(!predicate.test(methodReturnType))
			throw new DecompilationException("The method return type (" + methodReturnType + ")" +
					" does not match type of the `" + getInstructionName() + "` instruction");
		
		this.operand = context.stack.popAsNarrowest(methodReturnType);
	}
	
	
	protected abstract String getInstructionName();
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print("return ").print(operand, context);
	}
}