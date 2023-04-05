package x590.jdecompiler.operation;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.scope.Scope;
import x590.jdecompiler.type.GeneralCastingKind;
import x590.jdecompiler.type.Type;

public abstract class AbstractOperation implements Operation {
	
	private boolean removed, removedFromScope;
	
	@Override
	public void remove() {
		this.removedFromScope = this.removed = true;
	}
	
	@Override
	public void removeFromScope() {
		this.removedFromScope = true;
	}
	
	@Override
	public void unremove() {
		this.removed = false;
	}
	
	@Override
	public void unremoveFromScope() {
		this.removedFromScope = false;
	}
	
	@Override
	public boolean isRemoved() {
		return removed;
	}
	
	@Override
	public boolean isRemovedFromScope() {
		return removedFromScope;
	}
	
	@Override
	public final void allowImplicitCast() {
		setImplicitCast(true);
	}
	
	@Override
	public final void denyImplicitCast() {
		setImplicitCast(false);
	}
	
	protected void setImplicitCast(boolean implicitCast) {}
	
	@Override
	public final <T extends Type> T getReturnTypeAsNarrowest(T type) {
		@SuppressWarnings("unchecked")
		T newType = (T)getReturnType().castToNarrowest(type);
		onCastReturnType(newType);
		return newType;
	}
	
	@Override
	public final <T extends Type> T getReturnTypeAsWidest(T type) {
		@SuppressWarnings("unchecked")
		T newType = (T)getReturnType().castToWidest(type);
		onCastReturnType(newType);
		return newType;
	}
	
	@Override
	public final void castReturnTypeToNarrowest(Type type) {
		onCastReturnType(getReturnType().castToNarrowest(type));
	}
	
	@Override
	public final void castReturnTypeToWidest(Type type) {
		onCastReturnType(getReturnType().castToWidest(type));
	}
	
	
	@Override
	public final Type getReturnTypeAsGeneralNarrowest(Operation other, GeneralCastingKind kind) {
		Type generalType = this.getReturnType().castToGeneral(other.getReturnType(), kind);
		this.castReturnTypeToNarrowest(generalType);
		other.castReturnTypeToNarrowest(generalType);
		return generalType;
	}
	
	
	protected void onCastReturnType(Type type) {}
	
	/** Гарантирует, что операция является scope-ом */
	@Override
	public final boolean isScope() {
		return this instanceof Scope;
	}
	
	/** Оборачивает операцию в скобки, если её приоритет ниже, чем {@code thisPriority} */
	@Override
	public final void writePrioritied(StringifyOutputStream out, Operation operation, StringifyContext context, int thisPriority, Associativity associativity) {
		int otherPriority = operation.getPriority();
		
		if(otherPriority < thisPriority || (otherPriority == thisPriority && Associativity.byPriority(otherPriority) != associativity))
			out.print('(').print(operation, context).print(')');
		else
			out.print(operation, context);
	}
	
	@Override
	public boolean equals(Object other) {
		return this == other || other instanceof Operation operation && this.equals(operation);
	}
}
