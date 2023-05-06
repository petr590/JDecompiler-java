package x590.jdecompiler.operation;

import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.scope.Scope;
import x590.jdecompiler.type.CastingKind;
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
	public final Type getReturnTypeAsNarrowest(Type type) {
		return getReturnTypeAs(type, CastingKind.NARROWEST);
	}
	
	@Override
	public final Type getReturnTypeAsWidest(Type type) {
		return getReturnTypeAs(type, CastingKind.WIDEST);
	}
	
	@Override
	public final Type getReturnTypeAs(Type type, CastingKind casting) {
		Type newType = getReturnType().castTo(type, casting);
		onCastReturnType(newType, casting);
		return newType;
	}
	
	@Override
	public final void castReturnTypeToNarrowest(Type type) {
		castReturnTypeTo(type, CastingKind.NARROWEST);
	}
	
	@Override
	public final void castReturnTypeToWidest(Type type) {
		castReturnTypeTo(type, CastingKind.WIDEST);
	}
	
	@Override
	public final void castReturnTypeTo(Type type, CastingKind casting) {
		onCastReturnType(getReturnType().castTo(type, casting), casting);
	}
	
	
	@Override
	public final Type getReturnTypeAsGeneralNarrowest(Operation other, GeneralCastingKind kind) {
		Type generalType = getReturnType().castToGeneral(other.getReturnType(), kind);
		this.castReturnTypeToNarrowest(generalType);
		other.castReturnTypeToNarrowest(generalType);
		return generalType;
	}
	
	
	protected void onCastReturnType(Type type, CastingKind casting) {}
	
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
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
