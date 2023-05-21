package x590.jdecompiler.variable;

/**
 * Крч, переменная может быть empty, а может и нет.
 */
public interface EmptyableVariable {
	
	/** @return {@literal true}, если переменная пустая, {@literal false} в противном случае */
	public boolean isEmpty();
	
	/** @return {@literal true}, если переменная не пустая, {@literal false} в противном случае */
	public default boolean isNonEmpty() {
		return !isEmpty();
	}
	
	/** Возвращает {@literal this}, если переменная не пустая, иначе кидает исключение */
	public Variable nonEmpty();
	
	/** Определяет имя переменной */
	public void assignName();
	
	/** Возвращает имя переменной или {@literal null}, если имя ещё не определено */
//	public @Nullable String getName();
	
	/** Сведение типа переменной. При сведении типа мы определяем конечный тип переменной */
	public void reduceType();
	
	/** Оборачивает переменную */
	public EmptyableVariableWrapper wrapped();
	
	/** Разворачивает переменную */
	public default EmptyableVariable unwrapped() {
		return this;
	}
}
