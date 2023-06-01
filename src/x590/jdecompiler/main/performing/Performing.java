package x590.jdecompiler.main.performing;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import x590.jdecompiler.clazz.JavaClass;
import x590.util.annotation.Nullable;

/**
 * Класс, который предоставляет, что мы делаем с JavaClass:
 * декомиплируем, дизассемблируем, ассемблируем
 */
public interface Performing<S extends OutputStream> {
	
	/** Читает класс из указанного файла. Должен вызываться до {@link #setup()} */
	@Nullable JavaClass read(String filename) throws IOException, UncheckedIOException;
	
	/** Читает класс из указанного файла. При возникновении исключений
	 * IOException или UncheckedIOException выводит их в консоль и возвращает {@literal null} */
	default @Nullable JavaClass readSafe(String filename) {
		try {
			return read(filename);
		} catch(IOException | UncheckedIOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/** Устанавливает выходной поток, если необходимо.
	 * Должен вызываться до {@link #perform(JavaClass)} и после {@link #read(String)} или {@link #readSafe(String)} */
	void setup() throws IOException, UncheckedIOException;
	
	S getOutputStream();

	/** Осуществляет действие (например, декомпиляцию).
	 * Должен вызываться после {@link #setup()} и до {@link #afterPerforming(JavaClass)} */
	void perform(JavaClass clazz);

	/** Выполняет что-то после основного действия.
	 * Должен вызываться после {@link #perform(JavaClass)} и до {@link #write(JavaClass)} */
	void afterPerforming(JavaClass clazz);

	/** Записывает класс в поток.
	 * Должен вызываться после {@link #afterPerforming(JavaClass)} и до {@link #close()} */
	void write(JavaClass clazz) throws IOException, UncheckedIOException;

	/** Закрывает выходной поток.
	 * Должен вызываться после {@link #write(JavaClass)} */
	void close() throws IOException, UncheckedIOException;
}
