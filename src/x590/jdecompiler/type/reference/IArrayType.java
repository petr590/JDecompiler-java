package x590.jdecompiler.type.reference;

import x590.jdecompiler.type.Type;

/**
 * Описывет тип, который может быть массивом (а может и не быть)
 */
public interface IArrayType {
	
	/** @throws IllegalTypeException если тип не является массивом */
	public Type getMemberType();
	
	/** @throws IllegalTypeException если тип не является массивом */
	public Type getElementType();
	
	/**
	 * @param nestLevel - уровень вложенности элемента
	 * @return Вложенный тип элемента
	 * @throws IllegalArgumentException
	 * Если {@code nestLevel} меньше 1 или больше максимального уровня вложенности массива
	 */
	public default Type getNestedElementType(int nestLevel) {
		if(nestLevel <= 0) {
			throw new IllegalArgumentException("nestLevel " + nestLevel + " less than 1");
		}
		
		Type elementType = getNestedElementTypeRecursive(nestLevel);
		
		if(elementType == null) {
			throw new IllegalArgumentException("nestLevel " + nestLevel + " is too deep for array type " + this);
		}
		
		return elementType;
	}
	
	private Type getNestedElementTypeRecursive(int nestLevel) {
		if(nestLevel == 1) {
			return getElementType();
		}
		
		if(getElementType() instanceof IArrayType arrayElementType) {
			return arrayElementType.getNestedElementTypeRecursive(nestLevel - 1);
		}
		
		return null;
	}
}
