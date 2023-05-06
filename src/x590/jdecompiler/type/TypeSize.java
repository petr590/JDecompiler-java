package x590.jdecompiler.type;

/**
 * Размер типа на стеке (кратен 4 байтам)
 */
public enum TypeSize {
	VOID(0), // void
	WORD(1), // boolean, byte, short, char, int, float, Object
	LONG(2); // long, double
	
	private final int slotsOccupied;
	
	private TypeSize(int slotsOccupied) {
		this.slotsOccupied = slotsOccupied;
	}
	
	public int slotsOccupied() {
		return slotsOccupied;
	}
}
