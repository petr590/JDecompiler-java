package x590.javaclass.type;

public class CastStatus {
	
	/** Статус определяет приоритет при разрешении методов перегрузки.
	 * Чем меньше значение, тем выше статус */
	public static final int
			SAME = 1,				// Одинаковый тип
			EXTEND = 2,				// Расширение аргумента (String -> Object или char -> int)
			AUTOBOXING = 3,			// Автобоксинг (int -> Integer или Integer -> int)
			OBJECT_AUTOBOXING = 4,	// Автоупаковка в Object (int -> Integer -> Object)
			VARARGS = 5,				// Varargs
			NONE = Integer.MAX_VALUE / 2; // Конвертация невозможна
}