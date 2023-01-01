package x590.javaclass.operation;

import x590.javaclass.FieldDescriptor;
import x590.javaclass.Importable;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.constant.ConstOperation;
import x590.javaclass.operation.dup.AbstractDupOperation;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;
import x590.util.annotation.RemoveIfNotUsed;

/**
 * Класс, представляющий операцию. Операция - это объектное представление инструкций.<br>
 * Операция модет иметь операнды, которые представлены другими операциями.<br>
 * От операций наследуется класс Scope.<br>
 * Все обычные классы операций, которые не являются Scope-ами,
 * объявлены в пакете {@link x590.javaclass.operation} и его подпакетах.
 */
public abstract class Operation implements Importable {
	
	/**
	 * Ассоциативность (направленность) операций. Например, сложение
	 * выполняется слева направо, а приведение типов - справа налево.
	 */
	public enum Associativity {
		LEFT, RIGHT;
		
		public static Associativity byPriority(int priority) {
			return  priority == Priority.ASSIGNMENT || priority == Priority.TERNARY_OPERATOR ||
					priority == Priority.CAST || priority == Priority.UNARY ? RIGHT : LEFT;
		}
	}
	
	
	/** Метод записи в поток */
	public abstract void writeTo(StringifyOutputStream out, StringifyContext context);
	
	/** Метод записи в поток в качестве инициализатора массива */
	public void writeAsArrayInitializer(StringifyOutputStream out, StringifyContext context) {
		writeTo(out, context);
	}
	
	/** Вывод перед каждой операцией */
	public StringifyOutputStream printFront(StringifyOutputStream out, StringifyContext context) {
		return out.println().printIndent();
	}
	
	/** Вывод после каждой операции */
	public StringifyOutputStream printBack(StringifyOutputStream out, StringifyContext context) {
		return out.print(';');
	}
	
	/** Вывод между операцииями (т.е. для всех, кроме последней) */
	public void writeSeparator(StringifyOutputStream out, StringifyContext context) {}
	
	
	public boolean canOmit() {
		return false;
	}
	
	
	private boolean removed;
	
	/** Выставляет флаг {@link #removed}, не удаляет операцию физически из какого-то списка */
	public void remove() {
		removed = true;
	}
	
	/** Удалена ли операция, т.е. выставлен ли флаг {@link #removed} */
	public boolean isRemoved() {
		return removed;
	}
	
	
	/** Возвращаемый тип операции. Если тип - {@link PrimitiveType.VOID},
	 * то операция не будет добавлена на стек */
	public abstract Type getReturnType();
	
	/** Неявный тип, чтобы избежать лишних преобразований. Например, когда мы
	 * сохраняем значение int в переменную long, приведение типов можно не писать. */
	public Type getImplicitType() {
		return getReturnType();
	}
	
	/** Разрешает опустить явное преобразование */
	public void allowImplicitCast() {}
	
	/** Нужно для {@link ConstOperation} */
	@RemoveIfNotUsed
	public void setOwnerConstant(FieldDescriptor ownerConstant) {}
	
	
	public boolean implicitCastAllowed() {
		return getImplicitType() != getReturnType();
	}
	
	
	public <T extends Type> T getReturnTypeAsNarrowest(T type) {
		@SuppressWarnings("unchecked")
		T newType = (T)getReturnType().castToNarrowest(type);
		onCastReturnType(newType);
		return newType;
	}
	
	public <T extends Type> T getReturnTypeAsWidest(T type) {
		@SuppressWarnings("unchecked")
		T newType = (T)getReturnType().castToWidest(type);
		onCastReturnType(newType);
		return newType;
	}
	
	public void castReturnTypeToNarrowest(Type type) {
		onCastReturnType(getReturnType().castToNarrowest(type));
	}
	
	public void castReturnTypeToWidest(Type type) {
		onCastReturnType(getReturnType().castToWidest(type));
	}
	
	
	public Type getReturnTypeAsGeneralNarrowest(Operation other) {
		Type generalType = this.getReturnType().castToGeneral(other.getReturnType());
		this.castReturnTypeToNarrowest(generalType);
		other.castReturnTypeToNarrowest(generalType);
		return generalType;
	}
	
	
	public void onCastReturnType(Type type) {}
	
	/** Возвращает исходную операцию (для класса {@link AbstractDupOperation},
	 * для всех остальных классов возвращает {@literal this}) */
	public Operation original() {
		return this;
	}
	
	
	/** Возвращает {@literal true}, если операция использует локальные переменные */
	public boolean requiresLocalContext() {
		return false;
	}
	
	
	/** Гарантирует, что операция является scope-ом */
	public boolean isScope() {
		return false;
	}
	
	/** Проверяет, что операция является константой 1 (любого типа) */
	public boolean isOne() {
		return false;
	}
	
	
	/** Приоритет операции, используется для правильной расстановки скобок */
	public int getPriority() {
		return Priority.DEFAULT_PRIORITY;
	}
	
	public void writePrioritied(StringifyOutputStream out, Operation operation, StringifyContext context, Associativity associativity) {
		writePrioritied(out, operation, context, getPriority(), associativity);
	}
	
	/** Оборачивает операцию в скобки, если её приоритет ниже, чем {@code thisPriority} */
	public void writePrioritied(StringifyOutputStream out, Operation operation, StringifyContext context, int thisPriority, Associativity associativity) {
		int otherPriority = operation.getPriority();
		
		if(otherPriority < thisPriority || (otherPriority == thisPriority && Associativity.byPriority(otherPriority) != associativity))
			out.print('(').print(operation, context).print(')');
		else
			out.print(operation, context);
	}
	
	
	public void addVariableName(String name) {}
}
