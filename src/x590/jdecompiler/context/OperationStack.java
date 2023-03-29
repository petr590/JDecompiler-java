package x590.jdecompiler.context;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.EmptyStackException;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.stream.Stream;

import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.TypeSize;

public class OperationStack {
	
	private final Deque<Operation> stack;
	
	private Predicate<Operation> nextPushHandler;
	
	
	public OperationStack() {
		this.stack = new ArrayDeque<>();
	}
	
	public OperationStack(int initalCapacity) {
		this.stack = new ArrayDeque<>(initalCapacity);
	}
	
	
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	public int size() {
		return stack.size();
	}
	
	
	public Operation push(Operation operation) {
		if(nextPushHandler != null) {
			if(nextPushHandler.test(operation))
				stack.push(operation);
			
			nextPushHandler = null;
			return operation;
		}
		
		stack.push(operation);
		return operation;
	}
	
	public void pushAll(Collection<Operation> operations) {
		for(Operation operation : operations) {
			push(operation);
		}
	}
	
	
	public Operation pop() {
		try {
			return stack.pop();
		} catch(NoSuchElementException ex) {
			throw new EmptyStackException();
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
	
	
	public Operation peek() {
		try {
			return stack.peek();
		} catch(NoSuchElementException ex) {
			throw new EmptyStackException();
		}
	}
	
	public Operation peek(int index) {
		if(index < 0) {
			throw new IllegalArgumentException("index = " + index);
		}
		
		if(index >= stack.size()) {
			throw new IndexOutOfBoundsException("Index " + index + " out of range for stack size " + stack.size());
		}
		
		return stack.stream().limit(index + 1).reduce(null, (first, second) -> second);
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
	
	public void clearNextPushHandler() {
		this.nextPushHandler = null;
	}
	
	public Stream<Operation> stream() {
		return stack.stream();
	}
	
	@Override
	public String toString() {
		return stack.toString();
	}
	
	@Override
	public int hashCode() {
		return stack.hashCode();
	}
}
