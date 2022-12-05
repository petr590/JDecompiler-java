package x590.javaclass.operation;

import x590.javaclass.Variable;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;

public class StoreOperation extends Operation {
	private final int index;
	private final Variable variable;
	private final Operation value;

	protected Operation incOperation = null;
	
	public StoreOperation(Type type, DecompilationContext context, int index) {
		this.index = index;
		this.variable = context.currentScope().getVariableOrDefine(index, type);
		this.value = context.stack.pop();
		
		variable.castTypeToWidest(value.getReturnTypeAsNarrowest(type));
	}
	
	
	public int getIndex() {
		return index;
	}
	
	public Variable getVariable() {
		return variable;
	}
	
	public Operation getValue() {
		return value;
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print(variable.getName()).print(" = ").print(value, context);
	}
	
	@Override
	public Type getReturnType() {
		return PrimitiveType.VOID;
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