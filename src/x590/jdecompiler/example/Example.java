package x590.jdecompiler.example;

import x590.jdecompiler.FileSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Позволяет делать обработку примеров через аннотации
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Example {
	
	/** Классы, которые мы будем декомпилировать.
	 * Если массив пустой, значит, декомпилируется тот класс, в котором объявлена аннотация */
	Class<?>[] classes() default {};
	
	/** Массив аргументов для запуска декомпилятора */
	String[] args() default {};
	
	/** Папка, где будет поиск классов */
	String directory() default ExampleTesting.DEFAULT_DIR;
	
	/** Откуда брать классы */
	FileSource source() default FileSource.FILESYSTEM;
}
