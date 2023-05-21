package x590.jdecompiler.type;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.io.ExtendedOutputStream;
import x590.jdecompiler.type.primitive.PrimitiveType;

/** Что угодно, кроме {@literal boolean}. Дискриминация как она есть :/ */
public final class ExcludingBooleanType extends Type {
	
	public static final ExcludingBooleanType INSTANCE = new ExcludingBooleanType();
	
	
	private static final String NAME = "ExcludingBooleanType";
	
	private ExcludingBooleanType() {}
	
	
	@Override
	public void writeTo(ExtendedOutputStream<?> out, ClassInfo classinfo) {
		out.write(NAME);
	}
	
	@Override
	public String toString() {
		return NAME;
	}
	
	@Override
	public String getEncodedName() {
		return NAME;
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public String getNameForVariable() {
		return PrimitiveType.INT.getNameForVariable();
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.WORD;
	}
	
	@Override
	public boolean isDefinitelySubtypeOf(Type other) {
		return other != PrimitiveType.BOOLEAN;
	}
	
	@Override
	protected Type castImpl(Type other, CastingKind kind) {
		if(other instanceof UncertainIntegralType intergalType) {
			return !intergalType.includeBoolean() ?
					intergalType :
					UncertainIntegralType.getInstance(intergalType.minCapacity(), intergalType.maxCapacity(), false, intergalType.includeChar());
		}
		
		return other != PrimitiveType.BOOLEAN ? other : null;
	}
	
	@Override
	protected Type reversedCastImpl(Type other, CastingKind kind) {
		return castImpl(other, kind);
	}
	
	@Override
	public BasicType reduced() {
		return PrimitiveType.INT;
	}
}
