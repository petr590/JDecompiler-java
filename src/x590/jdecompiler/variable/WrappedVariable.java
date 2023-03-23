package x590.jdecompiler.variable;

import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.scope.Scope;
import x590.jdecompiler.type.Type;

public class WrappedVariable implements VariableWrapper {
	
	private EmptyableVariable variable;
	
	public WrappedVariable(EmptyableVariable variable) {
		this.variable = variable;
	}
	
	@Override
	public void makeSame(EmptyableVariable other) {
		this.variable = other.unwrapped();
	}
	
	@Override
	public EmptyableVariable unwrapped() {
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
	public String getName() {
		return variable.getName();
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
	public void addPossibleName(String name) {
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
		variable.nonEmpty().castTypeToNarrowest(newType);
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
	public String toString() {
		return "WrappedVariable { " + variable + " }";
	}
}
