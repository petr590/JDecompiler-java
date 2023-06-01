package x590.jdecompiler.operation.store;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.Priority;
import x590.jdecompiler.operation.increment.IncrementableOperation;
import x590.jdecompiler.operation.load.LoadOperation;
import x590.jdecompiler.operation.variable.OperationWithVariable;
import x590.jdecompiler.operation.variable.PossibleExceptionStoreOperation;
import x590.jdecompiler.operation.variable.VariableDefinitionOperation;
import x590.jdecompiler.type.CastingKind;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.variable.Variable;
import x590.util.annotation.Nullable;

public abstract class StoreOperation extends OperationWithVariable
		implements IncrementableOperation, VariableDefinitionOperation, PossibleExceptionStoreOperation {
	
	private final int slot;
	private Operation value;
	
	private IncrementData incData;
	private boolean isVarDefinition, isTypeHidden;
	
	
	public StoreOperation(Type requiredType, DecompilationContext context, int slot) {
		
		this.slot = slot;
		var value = this.value = context.popAsNarrowest(requiredType);
		
		value.allowImplicitCast();
		
		var variable = this.variable = removeIfExceptionLoadOperation(context, value) ?
				context.currentScope().defineNewVariable(slot, value.getReturnType(), context.currentIndex()) :
				context.currentScope().getVariableOrDefine(slot, context.currentIndex(), requiredType);
		
		variable.addPossibleName(value.getPossibleVariableName());
		
		variable.castTypeToWidest(value.getReturnType());
		variable.addAssignedOperation(value);
		
		this.value = value = value.useAsNarrowest(variable.getType());
		
		this.incData = init(context, value, variable.getType());
	}
	
	@Override
	protected Type getDeducedType(Type returnType) {
		variable.castTypeToWidest(value.getReturnType());
		value = value.useAsNarrowest(variable.getType());
		
		var preIncLoadOperation = incData.getPreIncLoadOperation();
		
		return preIncLoadOperation != null ?
				preIncLoadOperation.getReturnType() :
				returnType;
	}
	
	@Override
	public @Nullable Variable getStoringVariable() {
		return null;
	}
	
	public int getSlot() {
		return slot;
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

	@Override
	public IncrementData getIncData() {
		return incData;
	}

	@Override
	public void setIncData(IncrementData incData) {
		this.incData = incData;
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
		value.allowShortArrayInitializer();
		value.writeTo(out, context);
	}
	
	@Override
	public void onCastReturnType(Type newType, CastingKind kind) {
		super.onCastReturnType(newType, kind);
		variable.castTypeTo(newType, kind);
	}
	
	@Override
	public void addPossibleVariableName(String name) {
		variable.addPossibleName(name);
	}

	@Override
	public Operation inline(Int2ObjectMap<Operation> varTable) {
		throw new DecompilationException("Store operation must not appear in the inlining method");
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
