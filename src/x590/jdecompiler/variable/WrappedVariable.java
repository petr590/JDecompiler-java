package x590.jdecompiler.variable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.scope.Scope;
import x590.jdecompiler.type.CastingKind;
import x590.jdecompiler.type.Type;
import x590.util.annotation.Nullable;

public class WrappedVariable implements VariableWrapper {
	
	private Variable variable;
	
	public WrappedVariable(Variable variable) {
		this.variable = variable;
	}
	
	@Override
	public VariableWrapper assign(Variable other) {
		this.variable = other.unwrapped();
		return this;
	}
	
	@Override
	public Variable unwrapped() {
		return variable;
	}
	
	@Override
	public boolean isEmpty() {
		return variable.isEmpty();
	}
	
	@Override
	public VariableWrapper nonEmpty() {
		variable = variable.nonEmpty();
		return this;
	}
	
	@Override
	public boolean hasName() {
		return variable.hasName();
	}
	
	@Override
	public void assignName() {
		variable.assignName();
	}
	
	@Override
	public @Nullable String getPossibleName() {
		return variable.getPossibleName();
	}
	
	@Override
	public @Nullable String getNullableName() {
		return variable.getNullableName();
	}
	
	@Override
	public void reduceType() {
		variable.reduceType();
	}
	
	@Override
	public void setName(String name) {
		variable.nonEmpty().setName(name);
	}
	
	@Override
	public void addPossibleName(@Nullable String name) {
		variable.nonEmpty().addPossibleName(name);
	}
	
	@Override
	public void makeAnIndex() {
		variable.nonEmpty().makeAnIndex();
	}
	
	@Override
	public boolean isDefined() {
		return variable.nonEmpty().isDefined();
	}
	
	@Override
	public void define() {
		variable.nonEmpty().define();
	}
	
	@Override
	public Type getType() {
		return variable.nonEmpty().getType();
	}
	
	@Override
	public void setProbableType(Type probableType) {
		variable.nonEmpty().setProbableType(probableType);
	}
	
	@Override
	public void addAssignedOperation(Operation operation) {
		variable.nonEmpty().addAssignedOperation(operation);
	}
	
	@Override
	public void castTypeToNarrowest(Type newType) {
		variable.nonEmpty().castTypeToNarrowest(newType);
	}
	
	@Override
	public void castTypeToWidest(Type newType) {
		variable.nonEmpty().castTypeToWidest(newType);
	}
	
	@Override
	public void castTypeTo(Type newType, CastingKind kind) {
		variable.nonEmpty().castTypeTo(newType, kind);
	}
	
	@Override
	public Scope getEnclosingScope() {
		return variable.nonEmpty().getEnclosingScope();
	}
	
	@Override
	public void setEnclosingScope(Scope enclosingScope) {
		variable.nonEmpty().setEnclosingScope(enclosingScope);
	}
	
	@Override
	public @Nullable Int2ObjectMap<String> getEnumTable() {
		return variable.getEnumTable();
	}
	
	@Override
	public void setEnumTable(@Nullable Int2ObjectMap<String> enumMap) {
		variable.setEnumTable(enumMap);
	}
	
	@Override
	public String toString() {
		return "WrappedVariable { " + variable + " }";
	}
}
