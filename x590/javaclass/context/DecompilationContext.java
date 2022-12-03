package x590.javaclass.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import x590.javaclass.ClassInfo;
import x590.javaclass.MethodDescriptor;
import x590.javaclass.Modifiers;
import x590.javaclass.NamedVariable;
import x590.javaclass.UnnamedVariable;
import x590.javaclass.Variable;
import x590.javaclass.instruction.Instruction;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.VReturnOperation;
import x590.javaclass.scope.MethodScope;
import x590.javaclass.scope.Scope;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;

public class DecompilationContext extends DecompilationAndStringifyContext {
	
	public final OperationStack stack = new OperationStack();
	private final Stack<Scope> scopeStack = new Stack<>();
	public final List<Operation> operations;
	protected Scope currentScope;
	
	private final List<Variable> locals;
	
	private DecompilationContext(Context otherContext, ClassInfo classinfo, MethodDescriptor descriptor, int modifiers, MethodScope methodScope, List<Instruction> instructions, int maxLocals) {
		super(otherContext, classinfo, descriptor, modifiers);

		var locals = this.locals = new ArrayList<>(maxLocals);
		this.currentScope = methodScope;
		
		int i = descriptor.arguments.size();
		
		if(!Modifiers.isStatic(modifiers)) {
			locals.add(new NamedVariable("this", classinfo.thisType));
			i++;
		}
		
		for(Type argType : descriptor.arguments) {
			locals.add(new UnnamedVariable(argType));
		}
		
		for(; i < maxLocals; i++) {
			locals.add(new UnnamedVariable());
		}
		
		
		var stack = this.stack;
		List<Operation> operations = new ArrayList<>(instructions.size());
		
		for(Instruction instruction : instructions) {
			
			finalizeScopes();
			
			if(instruction != null) {
				
				Operation operation = instruction.toOperation(this);
				
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
	
	
	public Scope getCurrentScope() {
		return currentScope;
	}
	
	public Scope getSuperScope() {
		return scopeStack.peek();
	}
	
	
	private void finalizeScopes() {
		while(index >= currentScope.endIndex && !scopeStack.isEmpty()) {
			System.out.println(index + ": " + currentScope + " finalized");
			currentScope = scopeStack.pop();
		}
	}
	
	
	public static DecompilationContext decompile(Context otherContext, ClassInfo classinfo, MethodDescriptor descriptor, int modifiers, MethodScope methodScope, List<Instruction> instructions, int maxLocals) {
		return new DecompilationContext(otherContext, classinfo, descriptor, modifiers, methodScope, instructions, maxLocals);
	}
	
	public Variable getVariable(int index) {
		return locals.get(index);
	}
	
	@Override
	public void warning(String message) {
		System.err.print("Decompilation warning: ");
		System.err.println(message);
	}
}