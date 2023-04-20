package x590.jdecompiler.type;

public interface IArrayType {
	
	/** @throws IllegalTypeException если тип не является массивом */
	public Type getMemberType();
	
	/** @throws IllegalTypeException если тип не является массивом */
	public Type getElementType();
}
