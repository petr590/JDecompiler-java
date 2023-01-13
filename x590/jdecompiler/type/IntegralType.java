package x590.jdecompiler.type;

/**
 * IntegralType - это целочисленный знаковый тип, который занимает
 * 4 байта в стеке: {@literal int}, {@literal short} и {@literal byte}.
 * {@literal boolean} и {@literal char} не включены в этот список, они обрабатываются отдельно
 */
public abstract class IntegralType extends PrimitiveType {
	
	public IntegralType(String encodedName, String name, String nameForVariable) {
		super(encodedName, name, nameForVariable);
	}
	
	@Override
	public final TypeSize getSize() {
		return TypeSize.WORD;
	}
	
	@Override
	public final boolean isIntegral() {
		return true;
	}
	
	/** Фактический размер типа данных */
	public abstract int getCapacity();
	
	
	@Override
	protected boolean canCastTo(Type other) {
		return this == other ||
				other.isIntegral() && ((IntegralType)other).getCapacity() >= this.getCapacity();
	}
	
	@Override
	protected boolean canCastToWidest(Type other) {
		return this == other ||
				other.isIntegral() && ((IntegralType)other).getCapacity() <= this.getCapacity() ||
				other == PrimitiveType.CHAR && this.getCapacity() > CHAR_CAPACITY;
	}
	
	@Override
	public boolean isImplicitSubtypeOf(Type other) {
		return isSubtypeOf(other) || other.isPrimitive() &&
				(other == PrimitiveType.LONG || other == PrimitiveType.FLOAT || other == PrimitiveType.DOUBLE);
	}
}
