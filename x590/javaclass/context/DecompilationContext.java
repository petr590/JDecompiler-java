package x590.javaclass.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import x590.javaclass.ClassInfo;
import x590.javaclass.MethodDescriptor;
import x590.javaclass.exception.DecompilationException;
import x590.javaclass.instruction.Instruction;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.returning.VReturnOperation;
import x590.javaclass.scope.MethodScope;
import x590.javaclass.scope.Scope;
import x590.javaclass.type.PrimitiveType;

public class DecompilationContext extends DecompilationAndStringifyContext {
	
	public final OperationStack stack = new OperationStack();
	private final Stack<Scope> scopeStack = new Stack<>();
	public final List<Operation> operations;
	protected Scope currentScope;
	
	private DecompilationContext(Context otherContext, ClassInfo classinfo, MethodDescriptor descriptor, int modifiers, MethodScope methodScope, List<Instruction> instructions, int maxLocals) {
		super(otherContext, classinfo, descriptor, methodScope, modifiers);

		this.currentScope = methodScope;
		
		var stack = this.stack;
		List<Operation> operations = new ArrayList<>(instructions.size());
		
		for(Instruction instruction : instructions) {
			
			finalizeScopes();
			
			
//			System.out.println("Stack: " + stack.stream().map(operation -> operation.getClass().getSimpleName() + " [" + operation.getReturnType() + "]").collect(Collectors.joining(", ")));
			
			
			if(instruction != null) {
				
				Operation operation;
				
				try {
					operation = instruction.toOperation(this);
				} catch(Exception ex) {
					throw new DecompilationException("At index " + index, ex);
				}
				
				if(operation != null) {
					operations.add(operation);
					
					if(operation.getReturnType() == PrimitiveType.VOID) {
						if(operation != VReturnOperation.INSTANCE) {
							currentScope.addOperation(this, operation);
							
							if(operation instanceof Scope scope) {
								scopeStack.push(currentScope);
								currentScope = scope;
							}
						}
						
					} else {
						stack.push(operation);
					}
				}
				
				index++;
			}
		}
		
		methodScope.deleteRemovedOperations();
		
		this.operations = operations.stream().filter(operation -> !operation.isRemoved()).toList();
	}
	
	
	public Scope currentScope() {
		return currentScope;
	}
	
	public Scope superScope() throws EmptyStackException {
		return scopeStack.peek();
	}
	
	
	public Collection<Scope> getScopes() {
		return Collections.unmodifiableCollection(scopeStack);
	}
	
	
	private void finalizeScopes() {
		while(index >= currentScope.endIndex() && !scopeStack.isEmpty()) {
			System.out.println(index + ": " + currentScope + " finalized");
			currentScope = scopeStack.pop();
		}
	}
	
	
	public static DecompilationContext decompile(Context otherContext, ClassInfo classinfo, MethodDescriptor descriptor, int modifiers, MethodScope methodScope, List<Instruction> instructions, int maxLocals) {
		return new DecompilationContext(otherContext, classinfo, descriptor, modifiers, methodScope, instructions, maxLocals);
	}
	
	@Override
	public void warning(String message) {
		System.err.print("Decompilation warning: ");
		System.err.println(message);
	}
}