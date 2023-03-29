package x590.jdecompiler.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Если когда-нибудь я сделаю обработку через аннотации,
 * то этот класс пригодится
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Example {
	
	/** Классы, которые мы будем декомпилировать.
	 * Если массив пустой, значит, декомпилируется тот класс, в котором объявлена аннотация */
	public Class<?>[] classes() default {};
	
	/** Массив аргументов для запуска декомпилятора */
	public String[] args() default {};
	
	/** Папка, где будет поиск классов */
	public String directory() default ExampleTesting.DEFAULT_DIR;
	
	/** Откуда брать классы */
	public DecompilationSource source() default DecompilationSource.FILESYSTEM;
	
	public enum DecompilationSource {
		FILESYSTEM, JDK
	}
}
