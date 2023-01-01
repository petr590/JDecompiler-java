package x590.javaclass.type;

public class CastStatus {
	
	/** Статус определяет приоритет при разрешении методов перегрузки.
	 * Чем меньше значение, тем выше статус */
	public static final int
			SAME =              0, // Одинаковый тип
			EXTEND =            1, // Расширение аргумента (String -> Object или char -> int)
			AUTOBOXING =        2, // Автобоксинг (int -> Integer или Integer -> int)
			OBJECT_AUTOBOXING = 3, // Автоупаковка в Object (int -> Integer -> Object)
			VARARGS =           4, // Varargs
			NONE = 0xFFFF;        // Конвертация невозможна. Это число было выбрано так, чтобы оно было достаточно большим,
			                       // и при этом не давало переполнения при максимальном количестве аргументов
}