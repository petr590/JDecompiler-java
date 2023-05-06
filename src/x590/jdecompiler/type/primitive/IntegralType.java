package x590.jdecompiler.type.primitive;

import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.TypeSize;

/**
 * Целочисленный знаковый тип, который занимает 4 байта в стеке
 * ({@literal int}, {@literal short} и {@literal byte}).<br>
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
	
	/** Размер примитива в байтах */
	public abstract int getCapacity();
	
	
	@Override
	protected boolean canCastToNarrowest(Type other) {
		return this == other ||
				other instanceof IntegralType integralType && integralType.getCapacity() >= this.getCapacity();
	}
	
	@Override
	protected boolean canCastToWidest(Type other) {
		return this == other ||
				other instanceof IntegralType integralType && integralType.getCapacity() <= this.getCapacity() ||
				other == CHAR && this.getCapacity() > CHAR_CAPACITY;
	}
	
	@Override
	public boolean isImplicitSubtypeOf(Type other) {
		return isSubtypeOf(other) || other.isLongOrFloatOrDouble();
	}
}
