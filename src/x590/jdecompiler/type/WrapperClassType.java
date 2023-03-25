package x590.jdecompiler.type;

import java.util.function.BiFunction;

import x590.util.annotation.Nullable;
import x590.util.function.TriFunction;

public final class WrapperClassType extends ClassType {
	
	private final PrimitiveType primitiveType;
	
	WrapperClassType(String encodedName, Class<?> thisClass, PrimitiveType primitiveType) {
		super(encodedName, thisClass);
		this.primitiveType = primitiveType;
	}
	
	public PrimitiveType getPrimitiveType() {
		return primitiveType;
	}
	
	private @Nullable Type castToGeneralImpl(Type other, GeneralCastingKind kind,
			TriFunction<Type, Type, GeneralCastingKind, Type> func,
			BiFunction<Type, GeneralCastingKind, Type> defaultFunc) {
		
		
		switch(kind) {
			case EQUALS_COMPARASION -> {
				return this == other ? this : null;
			}
			
			default -> {
				if(this == other) {
					return kind == GeneralCastingKind.TERNARY_OPERATOR ?
							this : this.primitiveType;
				}
				
				if(other instanceof WrapperClassType wrapper) {
					return func.apply(this.primitiveType, wrapper.primitiveType, kind);
				}
				
				if(other instanceof PrimitiveType primitiveType) {
					return func.apply(this.primitiveType, primitiveType, kind);
				}
				
				return defaultFunc.apply(primitiveType, kind);
			}
		}
	}
	
	@Override
	public @Nullable Type castToGeneralNoexcept(Type other, GeneralCastingKind kind) {
		return castToGeneralImpl(other, kind, Type::castToGeneralNoexcept, super::castToGeneralNoexcept);
	}
	
	@Override
	public @Nullable Type implicitCastToGeneralNoexcept(Type other, GeneralCastingKind kind) {
		return castToGeneralImpl(other, kind, Type::implicitCastToGeneralNoexcept, super::implicitCastToGeneralNoexcept);
	}
}
