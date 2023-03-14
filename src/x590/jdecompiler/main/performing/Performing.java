package x590.jdecompiler.main.performing;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import x590.jdecompiler.JavaClass;
import x590.util.annotation.Nullable;

/**
 * Класс, который предоставляет, что мы делаем с JavaClass:
 * декомиплируем, дизассемблируем, ассемблируем
 */
public interface Performing<S extends OutputStream> {
	
	/** Читает класс из указанного файла */
	public @Nullable JavaClass read(String file) throws IOException, UncheckedIOException;
	
	/** Читает класс из указанного файла. При возникновении исключений
	 * IOException или UncheckedIOException выводит их в консоль и возвращает {@literal null} */
	public default @Nullable JavaClass readSafe(String file) {
		try {
			return read(file);
		} catch(IOException | UncheckedIOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public void setup() throws IOException, UncheckedIOException;
	
	public S getOutputStream();
	
	public void perform(JavaClass clazz);
	
	/** Записывает класс в поток */
	public void write(JavaClass clazz) throws IOException, UncheckedIOException;
	
	public void close() throws IOException, UncheckedIOException;
}
