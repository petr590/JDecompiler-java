package x590.javaclass.context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import x590.javaclass.ClassInfo;
import x590.javaclass.MethodDescriptor;
import x590.javaclass.Modifiers;
import x590.javaclass.exception.DecompilationException;
import x590.javaclass.instruction.Instruction;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.returning.VReturnOperation;
import x590.javaclass.scope.MethodScope;
import x590.javaclass.scope.Scope;
import x590.javaclass.type.PrimitiveType;

public class DecompilationContext extends DecompilationAndStringifyContext {
	
	public final OperationStack stack = new OperationStack();
	public final List<Operation> operations;
	protected Scope currentScope;
	
	private final Queue<Scope> scopesQueue = new LinkedList<>();
	
	private DecompilationContext(Context otherContext, ClassInfo classinfo, MethodDescriptor descriptor, Modifiers modifiers, MethodScope methodScope, List<Instruction> instructions, int maxLocals) {
		super(otherContext, classinfo, descriptor, methodScope, modifiers);
		
		this.currentScope = methodScope;
		
		var stack = this.stack;
		List<Operation> operations = new ArrayList<>(instructions.size());
		
		for(Instruction instruction : instructions) {
			
			finalizeScopes(index);
			startScopes(index);
			
			
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
							currentScope.addOperation(operation);
							
							if(operation instanceof Scope scope) {
								scopesQueue.add(scope);
							}
						}
						
					} else {
						stack.push(operation);
					}
				}
				
				index++;
			}
		}
		
		checkScopesBounds(index);
		finalizeScopes(Integer.MAX_VALUE);
		
		this.operations = operations.stream().filter(operation -> !operation.isRemoved()).toList();
	}
	
	
	/** Убирает все scope-ы, которые вышли за границу видимости или были удалены. */
	private void finalizeScopes(int index) {
		String strIndex = index == Integer.MAX_VALUE ? "End" : Integer.toString(index);
	
		while(currentScope != null && (currentScope.isRemoved() || index >= currentScope.endIndex())) {
			currentScope.deleteRemovedOperations();
			currentScope.finalizeScope(this);
			
			System.out.println(strIndex + ": " + currentScope + (currentScope.isRemoved() ? " removed" : " finalized"));
			
			currentScope = currentScope.superScope();
		}
		
	}
	
	/** Кладёт на стэк все scope-ы, до которых дошла очередь. */
	private void startScopes(int index) {
		
		for(Iterator<Scope> iter = scopesQueue.iterator(); iter.hasNext(); ) {
			Scope scope = iter.next();
			
			if(scope.isRemoved()) {
				iter.remove();
				
			} else if(index > scope.startIndex()) {
				
				assert scope.superScope() == currentScope;
				currentScope = scope;
				
				System.out.println(index + ": " + scope + " started");
				
				iter.remove();
			}
		}
	}
	
	private void checkScopesBounds(int lastIndex) {
		for(Scope scope = currentScope; scope != null; scope = scope.superScope()) {
			if(scope.endIndex() > lastIndex) {
				warning("Scope " + scope + " is out of bounds of the " + methodScope);
			}
		}
	}
	
	
	public Scope currentScope() {
		return currentScope;
	}
	
	public Scope superScope() {
		return currentScope.superScope();
	}
	
	public Iterable<Scope> getScopes() {
		
		return new Iterable<>() {
			
			public Iterator<Scope> iterator() {
				
				return new Iterator<>() {
					
					private Scope scope = currentScope;
					
					public boolean hasNext() {
						return scope != null;
					}
					
					public Scope next() {
						var scope = this.scope;
						this.scope = scope.superScope();
						return scope;
					}
					
				};
			}
		};
	}
	
	
	public void addScopeToQueue(Scope scope) {
		scopesQueue.add(scope);
	}
	
	
	public static DecompilationContext decompile(Context otherContext, ClassInfo classinfo, MethodDescriptor descriptor, Modifiers modifiers, MethodScope methodScope, List<Instruction> instructions, int maxLocals) {
		return new DecompilationContext(otherContext, classinfo, descriptor, modifiers, methodScope, instructions, maxLocals);
	}
	
	@Override
	public void warning(String message) {
		System.err.print("Decompilation warning: ");
		System.err.println(message);
	}
}
