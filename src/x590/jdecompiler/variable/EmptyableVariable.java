package x590.jdecompiler.variable;

/**
 * Переменная, которая может быть пустой, а может и нет.
 */
public interface EmptyableVariable {
	
	/** @return {@literal true}, если переменная пустая, {@literal false} в противном случае */
	boolean isEmpty();
	
	/** @return {@literal true}, если переменная не пустая, {@literal false} в противном случае */
	default boolean isNonEmpty() {
		return !isEmpty();
	}
	
	/** Возвращает {@literal this}, если переменная не пустая, иначе кидает исключение */
	Variable nonEmpty();
	
	/** Определяет имя переменной */
	void assignName();
	
	/** Сведение типа переменной. При сведении типа мы определяем конечный тип переменной */
	void reduceType();
	
	/** Оборачивает переменную */
	EmptyableVariableWrapper wrapped();
	
	/** Разворачивает переменную */
	default EmptyableVariable unwrapped() {
		return this;
	}
}
