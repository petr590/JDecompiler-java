package x590.javaclass;

/**
 * Описывает объект, который может импортировать какие-то классы
 */
public interface Importable {
	
	public default void addImports(ClassInfo classinfo) {}
}