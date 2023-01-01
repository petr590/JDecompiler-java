package x590.javaclass.operation.store;

import x590.javaclass.ClassInfo;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.IncrementableOperation;
import x590.javaclass.operation.OperationWithVariable;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.Priority;
import x590.javaclass.operation.load.LoadOperation;
import x590.javaclass.type.Type;

public abstract class StoreOperation extends OperationWithVariable implements IncrementableOperation {
	private final int index;
	private final Operation value;
	
	private final IncrementData incData;
	private boolean varDefined;
	
	
	public StoreOperation(Type requiredType, DecompilationContext context, int index) {
		super(context.currentScope().getVariableOrDefine(index, requiredType));
		
		this.index = index;
		var value = this.value = context.stack.pop();
		
		value.allowImplicitCast();
		
		variable.castTypeToWidest(value.getReturnTypeAsNarrowest(requiredType));
		variable.addAssignedOperation(value);
		
		value.castReturnTypeToNarrowest(variable.getType());
		
		this.incData = init(context, value, variable.getType());
		returnType = incData.returnType;
	}
	
	
	public int getIndex() {
		return index;
	}
	
	public Operation getValue() {
		return value;
	}
	
	
	@Override
	public boolean canIncrement() {
		return true;
	}
	
	@Override
	public boolean isLoadOperation(Operation operation) {
		return operation instanceof LoadOperation loadOperation && loadOperation.getVariable() == variable;
	}
	
	@Override
	public void setProbableType(Type probableType) {
		variable.setProbableType(probableType);
	}
	
	
	public boolean defineVariable() {
		if(incData.operator == null) {
			variable.define();
			varDefined = true;
		}
		
		return varDefined;
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		if(varDefined)
			classinfo.addImport(variable.getType());
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		writeTo(out, context, returnType, incData);
	}
	
	@Override
	public void writeName(StringifyOutputStream out, StringifyContext context) {
		if(varDefined)
			out.writesp(variable.getType(), context.classinfo);
			
		out.write(variable.getName());
	}
	
	@Override
	public void writeValue(StringifyOutputStream out, StringifyContext context) {
		out.write(value, context);
	}
	
	@Override
	public void onCastReturnType(Type newType) {
		variable.castTypeToWidest(newType);
	}
	
	@Override
	public void addVariableName(String name) {
		variable.addName(name);
	}
	
	@Override
	public boolean requiresLocalContext() {
		return true;
	}
	
	@Override
	public int getPriority() {
		return Priority.ASSIGNMENT;
	}
}