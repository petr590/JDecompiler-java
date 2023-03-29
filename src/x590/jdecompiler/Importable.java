package x590.jdecompiler;

import x590.jdecompiler.clazz.ClassInfo;

/**
 * Описывает объект, который может импортировать какие-то классы
 */
public interface Importable {
	
	public default void addImports(ClassInfo classinfo) {}
}
