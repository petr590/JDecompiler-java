package x590.jdecompiler.operation.load;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.AbstractOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.CastingKind;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.variable.Variable;
import x590.util.Logger;
import x590.util.annotation.Nullable;

public abstract class LoadOperation extends AbstractOperation {
	
	private final int slot;
	private final Variable variable;
	
	public LoadOperation(Type type, DecompilationContext context, int slot) {
		this.slot = slot;
		this.variable = context.currentScope().getDefinedVariable(slot);
		variable.castTypeToNarrowest(type);
	}
	
	
	public int getSlot() {
		return slot;
	}
	
	public Variable getVariable() {
		return variable;
	}
	
	
	@Override
	public void setEnumTable(@Nullable Int2ObjectMap<String> enumTable) {
		variable.setEnumTable(enumTable);
	}
	
	@Override
	public @Nullable Int2ObjectMap<String> getEnumTable(DecompilationContext context) {
		return variable.getEnumTable();
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.write(variable.getName());
	}
	
	@Override
	public Type getReturnType() {
		return variable.getType();
	}
	
	@Override
	public void onCastReturnType(Type newType, CastingKind kind) {
		variable.castTypeTo(newType, kind);
	}
	
	@Override
	public void addPossibleVariableName(String name) {
		variable.addPossibleName(name);
	}
	
	@Override
	public @Nullable String getPossibleVariableName() {
		String name = variable.getPossibleName();
		return "this".equals(name) ? null : name;
	}
	
	@Override
	public boolean requiresLocalContext() {
		return true;
	}

	@Override
	public Operation inline(Int2ObjectMap<Operation> varTable) {
		return varTable.get(slot);
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof LoadOperation operation &&
				variable.equals(operation.variable);
	}
	
	@Override
	public String toString() {
		return String.format("%s { %s }", getClass().getSimpleName(), variable);
	}
}
