package x590.jdecompiler.type;

/**
 * Размер типа на стеке
 */
public enum TypeSize {
	ZERO_BYTES(0),  // void
	FOUR_BYTES(1),  // byte, short, char, int, float, boolean, Object
	EIGHT_BYTES(2); // long, double
	
	private final int occupiedSlots;
	
	private TypeSize(int occupiedSlots) {
		this.occupiedSlots = occupiedSlots;
	}
	
	public int slotsOccupied() {
		return occupiedSlots;
	}
}
