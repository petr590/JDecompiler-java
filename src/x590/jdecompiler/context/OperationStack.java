package x590.jdecompiler.context;

import java.util.Collection;
import java.util.Stack;
import java.util.function.Predicate;

import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.TypeSize;

public class OperationStack extends Stack<Operation> {
	
	private static final long serialVersionUID = -8788056838805079743L;
	
	
	private Predicate<Operation> nextPushHandler;
	
	
	@Override
	public Operation push(Operation operation) {
		if(nextPushHandler != null) {
			if(nextPushHandler.test(operation))
				super.push(operation);
			
			nextPushHandler = null;
			return operation;
		}
		
		return super.push(operation);
	}
	
	public void pushAll(Collection<Operation> operations) {
		for(Operation operation : operations) {
			push(operation);
		}
	}
	
	
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
		
		throw new DecompilationException("Operation size not matches: expected " + size + ", got " + operation.getReturnType().getSize() + " (operation " + operation + ")");
	}
	
	
	public Operation peek(int index) {
		return get(this.size() - index - 1);
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
		
		throw new DecompilationException("Operation size not matches: expected " + size + ", got " + operation.getReturnType().getSize() + " (operation " + operation + ")");
	}
	
	public Operation peekWithSize(int index, TypeSize size) {
		Operation operation = peek(index);
		
		if(operation.getReturnType().getSize() == size)
			return operation;
		
		throw new DecompilationException("Operation size not matches: expected " + size + ", got " + operation.getReturnType().getSize() + " (operation " + operation + ")");
	}
	
	
	public void onNextPush(Predicate<Operation> nextPushHandler) {
		this.nextPushHandler = nextPushHandler;
	}
}
