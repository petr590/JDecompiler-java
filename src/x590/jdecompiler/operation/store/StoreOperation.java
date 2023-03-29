package x590.jdecompiler.operation.store;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.IncrementableOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.OperationWithVariable;
import x590.jdecompiler.operation.Priority;
import x590.jdecompiler.operation.VariableDefinitionOperation;
import x590.jdecompiler.operation.load.ExceptionLoadOperation;
import x590.jdecompiler.operation.load.LoadOperation;
import x590.jdecompiler.scope.CatchScope;
import x590.jdecompiler.type.Type;

public abstract class StoreOperation extends OperationWithVariable implements IncrementableOperation, VariableDefinitionOperation {
	
	private final int index;
	private final Operation value;
	
	private final IncrementData incData;
	private boolean isVarDefinition, isTypeHidden;
	
	
	public StoreOperation(Type requiredType, DecompilationContext context, int index) {
		
		this.index = index;
		var value = this.value = context.pop();
		
		value.allowImplicitCast();
		
		if(requiredType.isAnyReferenceType() && value instanceof ExceptionLoadOperation &&
				context.currentScope() instanceof CatchScope catchScope && context.currentIndex() == catchScope.startIndex() + 1) {
			
			this.variable = catchScope.defineNewVariable(index, value.getReturnType(), context.currentIndex());
			variable.addPossibleName("ex");
			
			this.remove();
			
		} else {
			this.variable = context.currentScope().getVariableOrDefine(index, context.currentIndex(), requiredType);
			variable.addPossibleName(value.getPossibleVariableName());
		}
		
		variable.castTypeToWidest(value.getReturnTypeAsNarrowest(requiredType));
		variable.addAssignedOperation(value);
		
		value.castReturnTypeToNarrowest(variable.getType());
		
		this.incData = init(context, value, variable.getType());
	}
	
	
	public int getIndex() {
		return index;
	}
	
	public Operation getValue() {
		return value;
	}
	
	@Override
	public boolean isVariableDefinition() {
		return isVarDefinition;
	}
	
	@Override
	public void hideTypeDefinition() {
		this.isTypeHidden = true;
	}
	
	@Override
	public boolean isLoadOperation(Operation operation) {
		return operation instanceof LoadOperation loadOperation && loadOperation.getVariable().equals(variable);
	}
	
	@Override
	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}
	
	@Override
	public void setProbableType(Type probableType) {
		variable.setProbableType(probableType);
	}
	
	
	public boolean defineVariable() {
		if(incData.getOperator() == null) {
			variable.define();
			isVarDefinition = true;
		}
		
		return isVarDefinition;
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		if(isVarDefinition && !isTypeHidden)
			classinfo.addImport(variable.getType());
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		writeTo(out, context, returnType, incData);
	}
	
	@Override
	public void writeName(StringifyOutputStream out, StringifyContext context) {
		if(isVarDefinition && !isTypeHidden) {
			out.print(variable.getType(), context.getClassinfo(), variable.getName());
		} else {
			out.write(variable.getName());
		}
	}
	
	@Override
	public void writeValue(StringifyOutputStream out, StringifyContext context) {
		value.writeAsArrayInitializer(out, context);
	}
	
	@Override
	public void onCastReturnType(Type newType) {
		variable.castTypeToWidest(newType);
	}
	
	@Override
	public void addPossibleVariableName(String name) {
		variable.addPossibleName(name);
	}
	
	@Override
	public boolean requiresLocalContext() {
		return true;
	}
	
	@Override
	public int getPriority() {
		return Priority.ASSIGNMENT;
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof StoreOperation operation &&
				super.equals(operation) && value.equals(operation.value);
	}
}
