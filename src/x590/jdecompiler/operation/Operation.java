package x590.jdecompiler.operation;

import java.util.LinkedList;

import x590.jdecompiler.FieldDescriptor;
import x590.jdecompiler.Importable;
import x590.jdecompiler.StringifyWritable;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.constant.ConstOperation;
import x590.jdecompiler.operationinstruction.constant.AConstNullOperationInstruction;
import x590.jdecompiler.type.GeneralCastingKind;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.ReferenceType;
import x590.jdecompiler.type.Type;
import x590.util.annotation.Nullable;
import x590.util.annotation.RemoveIfNotUsed;

/**
 * Класс, представляющий операцию. Операция - это объектное представление инструкций.<br>
 * Операция модет иметь операнды, которые представлены другими операциями.<br>
 * От операций наследуется класс Scope.<br>
 * Все обычные классы операций, которые не являются Scope-ами,
 * объявлены в пакете {@link x590.jdecompiler.operation} и его подпакетах.
 */
public interface Operation extends StringifyWritable<StringifyContext>, Importable {
	
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
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context);
	
	/** Метод записи в поток в качестве инициализатора массива */
	public default void writeAsArrayInitializer(StringifyOutputStream out, StringifyContext context) {
		writeTo(out, context);
	}
	
	/** Записывает операцию в поток как отдельное выражение */
	public default void writeAsStatement(StringifyOutputStream out, StringifyContext context) {
		this.writeFront(out, context);
		this.writeTo(out, context);
		this.writeBack(out, context);
	}
	
	/** Вывод перед каждой операцией */
	public default void writeFront(StringifyOutputStream out, StringifyContext context) {
		out.println().printIndent();
	}
	
	/** Вывод после каждой операции */
	public default void writeBack(StringifyOutputStream out, StringifyContext context) {
		out.write(';');
	}
	
	/** Вывод между операцииями (вызывается для всех, кроме последней) */
	public default void writeSeparator(StringifyOutputStream out, StringifyContext context, Operation nextOperation) {
		if(nextOperation.isScope())
			out.println();
	}
	
	
	public default boolean canOmit() {
		return false;
	}

	
	/**
	 * Отмечает операцию как удалённую, после чего она
	 * может быть удалена физически из списка операций
	 */
	public void remove();
	
	/**
	 * Отмечает операцию как не удалённую, однако если эта операция уже была
	 * удалена физически из какого-либо списка, то она не восстановится.
	 */
	public void unremove();
	
	/** Считается ли операция удалённой */
	public boolean isRemoved();
	
	
	/** Возвращаемый тип операции. Если тип - {@link PrimitiveType.VOID},
	 * то операция не будет добавлена на стек */
	public Type getReturnType();
	
	/** Неявный тип, чтобы избежать лишних преобразований. Например, когда мы
	 * сохраняем значение int в переменную long, приведение типов можно не писать. */
	public default Type getImplicitType() {
		return getReturnType();
	}
	
	/** Разрешает опустить явное преобразование */
	public void allowImplicitCast();
	
	/** Запрещает опустить явное преобразование */
	public void denyImplicitCast();
	
	/** Убирает неявное объявление массива при вызове метода с varargs */
	public default void inlineVarargs() {}
	
	/** Нужно для {@link ConstOperation} */
	@RemoveIfNotUsed
	public default void setOwnerConstant(FieldDescriptor ownerConstant) {}
	
	
	public default boolean implicitCastAllowed() {
		return getImplicitType() != getReturnType();
	}
	
	
	public <T extends Type> T getReturnTypeAsNarrowest(T type);
	
	public <T extends Type> T getReturnTypeAsWidest(T type);
	
	public void castReturnTypeToNarrowest(Type type);
	
	public void castReturnTypeToWidest(Type type);
	
	
	public Type getReturnTypeAsGeneralNarrowest(Operation other, GeneralCastingKind kind);
	
	
	/** Сведение типа */
	public default void reduceType() {}
	
	/** Делает преобразование для константы {@literal null},
	 * так как мы не можем обратиться к полю или методу напрямую через {@literal null}.
	 * @param clazz - тип, к которому преобразуется {@literal null}
	 * @see AConstNullOperationInstruction */
	public default Operation castIfNull(ReferenceType clazz) {
		return this;
	}
	
	
	/** Добавляет имя переменной (для операций, работающих с переменными) */
	public default void addPossibleVariableName(String name) {}
	
	/** @return Возможное имя для переменной. По умолчанию {@literal null} */
	public default @Nullable String getPossibleVariableName() {
		return null;
	}
	
	
	/** Возвращает {@literal true}, если операция использует локальные переменные */
	public default boolean requiresLocalContext() {
		return false;
	}
	
	
	/** Гарантирует, что операция является scope-ом */
	public boolean isScope();
	
	/** Проверяет, что операция является константой 1 (любого типа) */
	public default boolean isOne() {
		return false;
	}
	
	/**
	 * Проверяет, что операция объявляет переменную.
	 * Нужно для корректной декомпиляции такого кода:
		if(condition) {
			int x = 0;
		}
	 * Java не позволяет писать объявление переменной в блоке без фигурных скобок
	 */
	public default boolean isVariableDefinition() {
		return false;
	}
	
	
	/**
	 * Операции, код после которых не выполняется, такие как throw и return
	 */
	public default boolean isTerminable() {
		return false;
	}
	
	
	/**
	 * Проверяет, что операция является цепью вызовом {@code StringBuilder#append(...)}
	 * или {@code new StringBuilder(...)}. Если это так, добавляет операнд в список операндов.
	 * @return Список операндов, если цепь вызовов полностью распознана, иначе {@literal null}
	 */
	public default @Nullable LinkedList<Operation> getStringBuilderChain(LinkedList<Operation> operands) {
		return null;
	}
	
	
	public default void postDecompilation() {}
	
	
	/** Приоритет операции, используется для правильной расстановки скобок */
	public default int getPriority() {
		return Priority.DEFAULT_PRIORITY;
	}
	
	public default void writePrioritied(StringifyOutputStream out, Operation operation, StringifyContext context, Associativity associativity) {
		writePrioritied(out, operation, context, getPriority(), associativity);
	}
	
	/** Оборачивает операцию в скобки, если её приоритет ниже, чем {@code thisPriority} */
	public void writePrioritied(StringifyOutputStream out, Operation operation, StringifyContext context, int thisPriority, Associativity associativity);
	
	public boolean equals(Operation other);
}
