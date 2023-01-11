package x590.jdecompiler.context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.Importable;
import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.modifiers.MethodModifiers;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.returning.VReturnOperation;
import x590.jdecompiler.scope.MethodScope;
import x590.jdecompiler.scope.Scope;
import x590.jdecompiler.type.PrimitiveType;
import x590.util.Logger;

public class DecompilationContext extends DecompilationAndStringifyContext implements Importable {
	
	public final OperationStack stack = new OperationStack();
	protected final List<Operation> operations;
	protected Scope currentScope;
	
	private final Queue<Scope> scopesQueue = new LinkedList<>();
	
	private DecompilationContext(Context otherContext, ClassInfo classinfo, MethodDescriptor descriptor, MethodModifiers modifiers, MethodScope methodScope, List<Instruction> instructions, int maxLocals) {
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
			
			Logger.logf("%s: %s %s", strIndex, currentScope, currentScope.isRemoved() ? "removed" : "finalized");
			
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
				
				Logger.logf("%d: %s startted", index, scope);
				
				iter.remove();
			}
		}
	}
	
	private void checkScopesBounds(int lastIndex) {
		for(Scope scope = currentScope; scope != null; scope = scope.superScope()) {
			if(scope.endIndex() > lastIndex) {
				Logger.warningFormatted("Scope %s is out of bounds of the %s", scope, methodScope);
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
	
	
	public static DecompilationContext decompile(Context otherContext, ClassInfo classinfo, MethodDescriptor descriptor, MethodModifiers modifiers, MethodScope methodScope, List<Instruction> instructions, int maxLocals) {
		return new DecompilationContext(otherContext, classinfo, descriptor, modifiers, methodScope, instructions, maxLocals);
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		operations.forEach(operation -> operation.addImports(classinfo));
	}
	
	@Override
	public void warning(String message) {
		Logger.warning("Decompilation warning: " + message);
	}
}
