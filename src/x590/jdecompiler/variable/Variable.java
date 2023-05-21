package x590.jdecompiler.variable;

import java.util.Optional;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import x590.jdecompiler.attribute.LocalVariableTableAttribute.LocalVariableEntry;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.scope.Scope;
import x590.jdecompiler.type.CastingKind;
import x590.jdecompiler.type.Type;
import x590.util.annotation.Nullable;

public interface Variable extends EmptyableVariable {
	
	@Override
	public default boolean isEmpty() {
		return false;
	}
	
	@Override
	public default Variable nonEmpty() {
		return this;
	}
	
	/** Проверяет, определено ли имя переменной */
	public boolean hasName();
	
	/** Возвращает имя переменной или {@literal null}, если имя ещё не определено */
	public @Nullable String getName();
	
	/** Возвращает вероятное имя переменной или {@literal null}, если нет ни одного */
	public @Nullable String getPossibleName();
	
	/** Устанавливает имя переменной */
	public void setName(String name);
	
	/** Добавляет переменной возможное имя, если оно не {@literal null} */
	public void addPossibleName(@Nullable String name);
	
	/** Делает переменную индексом */
	public default void makeAnIndex() {}
	
	
	/** Была ли переменная объявлена */
	public boolean isDefined();
	
	/** Объявить переменную */
	public void define();
	
	/** Объявить переменную и вернуть {@literal this} */
	public default Variable defined() {
		define();
		return this;
	}
	
	
	public Type getType();
	
	public void setProbableType(Type probableType);
	
	public void addAssignedOperation(Operation operation);
	
	public void castTypeToNarrowest(Type newType);
	
	public void castTypeToWidest(Type newType);
	
	public void castTypeTo(Type newType, CastingKind kind);
	
	
	public Scope getEnclosingScope();
	
	public void setEnclosingScope(Scope enclosingScope);
	
	
	public @Nullable Int2ObjectMap<String> getEnumTable();
	
	public void setEnumTable(@Nullable Int2ObjectMap<String> enumTable);
	
	
	@Override
	public default VariableWrapper wrapped() {
		return new WrappedVariable(this);
	}
	
	@Override
	public default Variable unwrapped() {
		return this;
	}
	
	
	public static EmptyVariable empty() {
		return EmptyVariable.INSTANCE;
	}
	
	
	public static AbstractVariable valueOf(Optional<LocalVariableEntry> optionalEntry, Scope enclosingScope, Type type, boolean typeFixed) {
		return optionalEntry.isEmpty() ?
				new UnnamedVariable(enclosingScope, type, typeFixed) :
				new NamedVariable(optionalEntry.get().getName(), enclosingScope, type, typeFixed);
	}
}
