package x590.jdecompiler.type;

/**
 * Содержит значения статуса преобразования, которые определяют
 * приоритет при разрешении методов перегрузки и выборе констант.
 * Чем меньше значение, тем выше статус
 */
public final class CastStatus {
	
	private CastStatus() {}
	
	/** Одинаковый тип */
	public static final int SAME = 0;
	
	/** Расширение аргумента (String -> Object или char -> int) */
	public static final int EXTEND = 1;
	
	/** Автобоксинг (int -> Integer или Integer -> int) */
	public static final int AUTOBOXING = 2;
	
	/** Автоупаковка в Object (int -> Integer -> Object) */
	public static final int OBJECT_AUTOBOXING = 3;
	
	/** Varargs */
	public static final int VARARGS = 4;
	
	/**
	 * Конвертация невозможна. Это число было выбрано так, чтобы оно было достаточно большим,
	 * и при этом не давало переполнения при максимальном количестве аргументов
	 */
	public static final int NONE = 0xFFFF; 
	
	
	public static boolean isNone(int status) {
		return status >= NONE;
	}
}
