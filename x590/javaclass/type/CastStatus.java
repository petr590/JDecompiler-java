package x590.javaclass.type;

public class CastStatus {

	/** Статус определяет приоритет при разрешении методов перегрузки.
	 * Чем меньше значение, тем выше статус.
	 * N_STATUS означает, что тип не имеет преобразования в другой тип */
	public static final int
			N_STATUS = Integer.MAX_VALUE,
			SAME_STATUS = 1,
			EXTEND_STATUS = 2, // Расширение аргумента (String -> Object или char -> int)
			AUTOBOXING_STATUS = 3,
			OBJECT_AUTOBOXING_STATUS = 4, // Автоупаковка в Object (int -> Integer -> Object)
			VARARGS_STATUS = 5;

}