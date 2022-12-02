package x590.javaclass.context;

import java.util.Stack;

import x590.javaclass.exception.DecompilationException;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.Type;
import x590.javaclass.type.TypeSize;

public class OperationStack extends Stack<Operation> {
	
	private static final long serialVersionUID = -8788056838805079743L;
	
	public Operation popAsNarrowest(Type requiredType) {
		Operation operation = pop();
		operation.castReturnTypeToNarrowest(requiredType);
		return operation;
	}
	
	public Operation popAsWidest(Type requiredType) {
		Operation operation = pop();
		operation.castReturnTypeToWidest(requiredType);
		return operation;
	}

	public Operation popWithSize(TypeSize size) {
		Operation operation = pop();
		
		if(operation.getReturnType().getSize() == size)
			return operation;
		
		throw new DecompilationException("Operation size not matches: expected " + size + ", got " + operation.getReturnType().getSize());
	}

	
	public Operation peekAsNarrowest(Type requiredType) {
		Operation operation = peek();
		operation.castReturnTypeToNarrowest(requiredType);
		return operation;
	}
	
	public Operation peekAsWidest(Type requiredType) {
		Operation operation = peek();
		operation.castReturnTypeToWidest(requiredType);
		return operation;
	}

	public Operation peekWithSize(TypeSize size) {
		Operation operation = peek();
		
		if(operation.getReturnType().getSize() == size)
			return operation;
		
		throw new DecompilationException("Operation size not matches: expected " + size + ", got " + operation.getReturnType().getSize());
	}
}