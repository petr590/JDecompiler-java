package x590.jdecompiler.operation.variable;

import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.variable.Variable;

public interface VariableDefinitionOperation extends Operation {
	
	/**
	 * Скрывает объявление типа переменной. Нужно, например,
	 * для корректной инициализации в начале цикла for
	 */
	public void hideTypeDefinition();

	public Variable getVariable();
}
