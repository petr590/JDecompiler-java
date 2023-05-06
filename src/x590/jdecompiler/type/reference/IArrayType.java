package x590.jdecompiler.type.reference;

import x590.jdecompiler.type.Type;

public interface IArrayType {
	
	/** @throws IllegalTypeException если тип не является массивом */
	public Type getMemberType();
	
	/** @throws IllegalTypeException если тип не является массивом */
	public Type getElementType();
}
