package x590.jdecompiler.type;

/**
 * Размер типа на стеке
 */
public enum TypeSize {
	VOID(0), // void
	WORD(1), // byte, short, char, int, float, boolean, Object
	LONG(2); // long, double
	
	private final int occupiedSlots;
	
	private TypeSize(int occupiedSlots) {
		this.occupiedSlots = occupiedSlots;
	}
	
	public int slotsOccupied() {
		return occupiedSlots;
	}
}
