package x590.javaclass.type;

/**
 * Размер типа на стеке
 */
public enum TypeSize {
	ZERO_BYTES, // void
	FOUR_BYTES, // byte, short, char, int, float, boolean, Object
	EIGHT_BYTES // long, double
}