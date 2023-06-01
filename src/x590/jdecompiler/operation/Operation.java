package x590.jdecompiler.operation;

import static x590.jdecompiler.operation.Priority.*;

import java.lang.reflect.Field;
import java.util.LinkedList;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import x590.jdecompiler.Importable;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.modifiers.MethodModifiers;
import x590.jdecompiler.operation.load.ALoadOperation;
import x590.jdecompiler.operationinstruction.constant.AConstNullOperationInstruction;
import x590.jdecompiler.type.CastingKind;
import x590.jdecompiler.type.GeneralCastingKind;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.primitive.PrimitiveType;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.jdecompiler.writable.StringifyWritable;
import x590.util.Logger;
import x590.util.annotation.Nullable;
import x590.util.holder.BooleanHolder;

/**
 * Класс, представляющий операцию. Операция - это объектное представление инструкций.<br>
 * Операция модет иметь операнды, которые представлены другими операциями.<br>
 * От операций наследуется класс Scope.<br>
 * Все обычные классы операций, которые не являются Scope-ами,
 * объявлены в пакете {@link x590.jdecompiler.operation} и его подпакетах.
 */
public interface Operation extends StringifyWritable<StringifyContext>, Importable, Cloneable {

	/**
	 * Ассоциативность (направленность) операций. Например, сложение
	 * выполняется слева направо, а приведение типов - справа налево.
	 */
	enum Associativity {
		LEFT, RIGHT;

		public static Associativity byPriority(int priority) {
			return switch(priority) {
				case ASSIGNMENT, TERNARY_OPERATOR, CAST, UNARY -> RIGHT;
				default -> LEFT;
			};
		}
	}


	/** Метод записи в поток */
	@Override
	void writeTo(StringifyOutputStream out, StringifyContext context);

	/** Записывает операцию в поток как отдельное выражение */
	default void writeAsStatement(StringifyOutputStream out, StringifyContext context) {
		this.writeFront(out, context);
		this.writeTo(out, context);
		this.writeBack(out, context);
	}

	/** Вывод перед каждой операцией */
	default void writeFront(StringifyOutputStream out, StringifyContext context) {
		out.println().printIndent();
	}

	/** Вывод после каждой операции */
	default void writeBack(StringifyOutputStream out, StringifyContext context) {
		out.write(';');
	}

	/** Вывод между операцииями (вызывается для всех операций в scope, кроме последней) */
	default void writeSeparator(StringifyOutputStream out, StringifyContext context, Operation nextOperation) {
		if(nextOperation.isScope())
			out.println();
	}


	/** Можно ли опустить операцию (например, вызоов суперконструктора по умолчанию) */
	default boolean canOmit() {
		return false;
	}


	/**
	 * Отмечает операцию как удалённую, после чего она
	 * может быть удалена физически из списка операций
	 */
	void remove();

	/**
	 * Отмечает операцию как удалённую из содержащего scope, после чего она
	 * может быть удалена физически из scope. Не удаляется из списка всех операций, в {@link DecompilationContext}
	 * т.е. для этой операции так же будет вызываться метод {@link #addImports(x590.jdecompiler.clazz.ClassInfo)}
	 */
	void removeFromScope();

	/**
	 * Отмечает операцию как не удалённую, однако если эта операция уже была
	 * удалена физически из какого-либо списка, то она не восстановится.
	 */
	void unremove();

	/**
	 * Отмечает операцию как не удалённую из содержащего scope, однако если эта операция уже была
	 * удалена физически из какого-либо scope, то она не восстановится.
	 */
	void unremoveFromScope();


	/** Считается ли операция удалённой */
	boolean isRemoved();

	/** Считается ли операция удалённой из содержащего scope */
	boolean isRemovedFromScope();


	/** Возвращаемый тип операции. Если тип - {@link PrimitiveType#VOID},
	 * то операция не будет добавлена на стек */
	Type getReturnType();

	/** Неявный тип, чтобы избежать лишних преобразований. Например, когда мы
	 * сохраняем значение int в переменную long, приведение типов можно не писать. */
	default Type getImplicitType() {
		return getReturnType();
	}

	/** Разрешает записывать инициализатор массива без указания {@code new <type>[]} */
	default void allowShortArrayInitializer() {}

	/** Разрешает опустить явное преобразование */
	void allowImplicitCast();

	/** Запрещает опустить явное преобразование */
	void denyImplicitCast();


	default int getAddingIndex(DecompilationContext context) {
		return context.currentIndex();
	}


	default boolean implicitCastAllowed() {
		return getImplicitType() != getReturnType();
	}


	@Deprecated(since = "0.8.8", forRemoval = true)
	/** Call {@link #useAsNarrowest(Type)} and then {@link #getReturnType()} instead */
	default Type getReturnTypeAsNarrowest(Type type) {
		return getReturnTypeAs(type, CastingKind.NARROWEST);
	}

	@Deprecated(since = "0.8.8", forRemoval = true)
	/** Call {@link #useAsWidest(Type)} and then {@link #getReturnType()} instead */
	default Type getReturnTypeAsWidest(Type type) {
		return getReturnTypeAs(type, CastingKind.WIDEST);
	}

	@Deprecated(since = "0.8.8", forRemoval = true)
	/** Call {@link #useAs(Type, CastingKind)} and then {@link #getReturnType()} instead */
	Type getReturnTypeAs(Type type, CastingKind kind);


	@Deprecated(since = "0.8.8", forRemoval = true)
	/** Call {@link #useAsNarrowest(Type)} instead */
	default void castReturnTypeToNarrowest(Type type) {
		castReturnTypeTo(type, CastingKind.NARROWEST);
	}

	@Deprecated(since = "0.8.8", forRemoval = true)
	/** Call {@link #useAsWidest(Type)} instead */
	default void castReturnTypeToWidest(Type type) {
		castReturnTypeTo(type, CastingKind.WIDEST);
	}

	@Deprecated(since = "0.8.8", forRemoval = true)
	/** Call {@link #useAs(Type, CastingKind)} instead */
	void castReturnTypeTo(Type type, CastingKind kind);


	@Deprecated(since = "0.8.8", forRemoval = true)
	Type getReturnTypeAsGeneralNarrowest(Operation other, GeneralCastingKind kind);


	default Operation useAsNarrowest(Type type) {
		return useAs(type, CastingKind.NARROWEST);
	}

	default Operation useAsWidest(Type type) {
		return useAs(type, CastingKind.WIDEST);
	}

	Operation useAs(Type type, CastingKind kind);


	/** Выведение типа. Первый проход выполняется при создании операций, т.е. собственно при декомпиляции.
	 * На втором проходе вызывается этот метод.
	 * @return {@literal true}, если тип сведён, иначе {@literal false} */
	default boolean deduceType() {
		return false;
	}

	/** Сведение типа операции */
	default void reduceType() {}

	/** Делает приведение типа для таких выражений, как {@literal null} или лямбда,
	 * так как мы не можем обратиться к полю или методу напрямую без приведения.
	 * @param clazz - тип, к которому преобразуется выражение */
	default Operation castIfNecessary(ReferenceType clazz) {
		return this;
	}

	/** Делает приведение типа для выражения {@literal null},
	 * так как мы не можем обратиться к полю или методу напрямую без приведения.
	 * @param clazz - тип, к которому преобразуется выражение
	 * @see AConstNullOperationInstruction */
	default Operation castIfNull(ReferenceType clazz) {
		return this;
	}


	/** Добавляет имя переменной (для операций, работающих с переменными) */
	default void addPossibleVariableName(String name) {}

	/** @return Возможное имя для переменной. По умолчанию {@literal null} */
	default @Nullable String getPossibleVariableName() {
		return null;
	}


	/** Возвращает {@literal true}, если операция использует локальные переменные */
	default boolean requiresLocalContext() {
		return false;
	}


	/** Гарантирует, что операция является scope-ом */
	boolean isScope();

	/** Проверяет, что операция является константой 1 (любого типа) */
	default boolean isOne() {
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
	default boolean isVariableDefinition() {
		return false;
	}

	/**
	 * Операции, код после которых не выполняется, такие как throw и return
	 */
	default boolean isTerminable() {
		return false;
	}

	/**
	 * Можно ли написать лямбду без фигурных скобок
	 */
	default boolean canInlineInLambda() {
		return true;
	}

	/**
	 * Гарантирует, что операция является объектом {@code this}.
	 */
	default boolean isThisObject() {
		return this instanceof ALoadOperation aload && aload.getSlot() == 0;
	}

	/**
	 * Гарантирует, что операция является объектом {@code this}.
	 * Также проверяет, что операция в нестатическом контексте
	 */
	default boolean isThisObject(MethodModifiers modifiers) {
		return modifiers.isNotStatic() && isThisObject();
	}


	/**
	 * @return Таблицу значений enum, необходимых для правильной работы {@literal switch}
	 * или {@literal null}, если операция не содержит таблицу.
	 */
	default @Nullable Int2ObjectMap<String> getEnumTable(DecompilationContext context) {
		return null;
	}

	/**
	 * Задаёт таблицу значений enum, необходимых для правильной работы {@literal switch},
	 * если операция поддержвает это
	 */
	default void setEnumTable(@Nullable Int2ObjectMap<String> enumTable) {}

	/**
	 * @return Встроенную операцию, т.е. операцию, в которой все использования переменных
	 *         заменены на операции по таблице {@code varTable}. Не изменяет исходную операцию.
	 *         Может вернуть {@literal this}, если встроенная операция не отличается от {@literal this}.
	 * @param varTable - таблица переменных. Ключ - слот переменной, значение - оперция,
	 *                   на которую эта переменная заменяется
	 * Реализация по умолчанию работает через рефлексию, так что класс операции в модуле,
	 * который не открывает доступ к приватным полям для модуля jdecompiler, должен переопределять
	 * метод
	 */
	default Operation inline(Int2ObjectMap<Operation> varTable) {
		Class<?> clazz = this.getClass();

		try {
			var inlined = inline(clazz, this, varTable, new BooleanHolder());

//			Logger.debugf("Inlined %s (%X) : %s (%X)", this, hashCode(), inlined, inlined.hashCode());

			return inlined;

		} catch(IllegalAccessException ex) {
			throw new IllegalStateException("Class " + clazz.getCanonicalName() +
					" in module " + clazz.getModule() + " must override the inline(Int2ObjectMap<Operation>) method" +
					" because its module does not opens access for the " + Operation.class.getModule() + " module");

		} catch(InstantiationException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static Operation inline(Class<?> clazz, Operation self, Int2ObjectMap<Operation> varTable, BooleanHolder cloned)
			throws IllegalAccessException, InstantiationException {

		if(!Operation.class.isAssignableFrom(clazz)) {
			return self;
		}

		for(Field field : clazz.getDeclaredFields()) {
			if(Operation.class.isAssignableFrom(field.getType())) {
				field.setAccessible(true);
				Operation operation = (Operation)field.get(self);
				Operation inlined = operation.inline(varTable);

				if(operation != inlined) {

					if(cloned.isFalse()) {
						cloned.set(true);

						self = self.clone();
					}

					field.set(self, inlined);
				}
			}
		}

		return inline(clazz.getSuperclass(), self, varTable, cloned);
	}


	/**
	 * Проверяет, что операция является цепью вызовов {@code StringBuilder#append(...)}
	 * или {@code new StringBuilder(...)}. Если это так, добавляет операнд в список операндов.
	 * @return Список операндов, если цепь вызовов полностью распознана, иначе {@literal null}
	 */
	default @Nullable LinkedList<Operation> getStringBuilderChain(LinkedList<Operation> operands) {
		return null;
	}


	/**
	 * Выполняется после основной декомпиляции кода
	 */
	default void postDecompilation(DecompilationContext context) {}


	/**
	 * Выполняется после декомпиляции всех классов
	 */
	default void afterDecompilation(DecompilationContext context) {}


	/** Приоритет операции, используется для правильной расстановки скобок */
	default int getPriority() {
		return Priority.DEFAULT_PRIORITY;
	}

	default void writePrioritied(StringifyOutputStream out, Operation operation, StringifyContext context, Associativity associativity) {
		writePrioritied(out, operation, context, getPriority(), associativity);
	}

	/** Оборачивает операцию в скобки, если её приоритет ниже, чем {@code thisPriority} */
	void writePrioritied(StringifyOutputStream out, Operation operation, StringifyContext context, int thisPriority, Associativity associativity);

	boolean equals(Operation other);

	/** Нужен для копирования объекта в методе {@link #inline(Int2ObjectMap)}.
	 * Создаёт поверхностную копию объекта */
	Operation clone();
}
