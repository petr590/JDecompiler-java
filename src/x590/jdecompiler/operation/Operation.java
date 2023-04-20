package x590.jdecompiler.operation;

import java.util.LinkedList;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import x590.jdecompiler.Importable;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.field.FieldDescriptor;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.modifiers.MethodModifiers;
import x590.jdecompiler.operation.constant.ConstOperation;
import x590.jdecompiler.operation.load.ALoadOperation;
import x590.jdecompiler.operationinstruction.constant.AConstNullOperationInstruction;
import x590.jdecompiler.type.GeneralCastingKind;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.ReferenceType;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.writable.StringifyWritable;
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
	
	
	/** Можно ли опустить операцию (например, вызоов суперконструктора по умолчанию) */
	public default boolean canOmit() {
		return false;
	}

	
	/**
	 * Отмечает операцию как удалённую, после чего она
	 * может быть удалена физически из списка операций
	 */
	public void remove();
	
	/**
	 * Отмечает операцию как удалённую из содержащего scope, после чего она
	 * может быть удалена физически из scope. Не удаляется из списка всех операций, в {@link DecompilationContext}
	 * т.е. для этой операции так же будет вызываться метод {@link #addImports(x590.jdecompiler.clazz.ClassInfo)}
	 */
	public void removeFromScope();
	
	/**
	 * Отмечает операцию как не удалённую, однако если эта операция уже была
	 * удалена физически из какого-либо списка, то она не восстановится.
	 */
	public void unremove();
	
	/**
	 * Отмечает операцию как не удалённую из содержащего scope, однако если эта операция уже была
	 * удалена физически из какого-либо scope, то она не восстановится.
	 */
	public void unremoveFromScope();
	
	
	/** Считается ли операция удалённой */
	public boolean isRemoved();
	
	/** Считается ли операция удалённой из содержащего scope */
	public boolean isRemovedFromScope();
	
	
	/** Возвращаемый тип операции. Если тип - {@link PrimitiveType.VOID},
	 * то операция не будет добавлена на стек */
	public Type getReturnType();
	
	/** Неявный тип, чтобы избежать лишних преобразований. Например, когда мы
	 * сохраняем значение int в переменную long, приведение типов можно не писать. */
	public default Type getImplicitType() {
		return getReturnType();
	}
	
	/** Разрешает записывать инициализатор массива без указания {@code new <type>[]} */
	public default void allowShortArrayInitializer() {}
	
	/** Разрешает опустить явное преобразование */
	public void allowImplicitCast();
	
	/** Запрещает опустить явное преобразование */
	public void denyImplicitCast();
	
	/** Нужно для {@link ConstOperation} */
	@RemoveIfNotUsed
	public default void setOwnerConstant(FieldDescriptor ownerConstant) {}

	
	public default int getAddingIndex(DecompilationContext context) {
		return context.currentIndex();
	}
	
	
	public default boolean implicitCastAllowed() {
		return getImplicitType() != getReturnType();
	}
	
	
	public Type getReturnTypeAsNarrowest(Type type);
	
	public Type getReturnTypeAsWidest(Type type);
	
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
	 * Можно ли написать лямбду без фигурных скобок
	 */
	public default boolean canInlineInLambda() {
		return true;
	}
	
	/**
	 * Гарантирует, что операция является объектом {@code this}.
	 */
	public default boolean isThisObject() {
		return this instanceof ALoadOperation aload && aload.getIndex() == 0;
	}
	
	/**
	 * Гарантирует, что операция является объектом {@code this}.
	 * Также проверяет, что операция в нестатическом контексте
	 */
	public default boolean isThisObject(MethodModifiers modifiers) {
		return modifiers.isNotStatic() && isThisObject();
	}
	
	
	/**
	 * @return Таблицу enum значений, необходимых для правильной работы {@literal switch}
	 * или {@literal null}, если операция не содержит таблицу.
	 */
	public default @Nullable Int2ObjectMap<String> getEnumTable(DecompilationContext context) {
		return null;
	}
	
	/**
	 * Задаёт таблицу enum значений, необходимых для правильной работы {@literal switch},
	 * если операция поддержвает это
	 */
	public default void setEnumTable(@Nullable Int2ObjectMap<String> enumTable) {}
	
	
	/**
	 * Проверяет, что операция является цепью вызовом {@code StringBuilder#append(...)}
	 * или {@code new StringBuilder(...)}. Если это так, добавляет операнд в список операндов.
	 * @return Список операндов, если цепь вызовов полностью распознана, иначе {@literal null}
	 */
	public default @Nullable LinkedList<Operation> getStringBuilderChain(LinkedList<Operation> operands) {
		return null;
	}
	
	
	/**
	 * Выполняется после основной декомпиляции кода
	 */
	public default void postDecompilation(DecompilationContext context) {}
	
	
	/**
	 * Выполняется после декомпиляции всех методов в классе
	 */
	public default void afterDecompilation(DecompilationContext context) {}
	
	
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
