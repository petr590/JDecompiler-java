package x590.jdecompiler;

/**
 * Описывает объект, который может импортировать какие-то классы
 */
public interface Importable {
	
	public default void addImports(ClassInfo classinfo) {}
}
