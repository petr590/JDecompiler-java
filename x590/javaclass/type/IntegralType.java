package x590.javaclass.type;

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
		return TypeSize.FOUR_BYTES;
	}
	
	@Override
	public final boolean isIntegral() {
		return true;
	}
	
	/** Фактический размер типа данных */
	public abstract int getCapacity();
}