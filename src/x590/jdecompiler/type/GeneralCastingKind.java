package x590.jdecompiler.type;

/**
 * Вид преобразования типов к общему типу. Указывает, в каком контексте происходит преобразование
 * к общему типу. Используется в методе {@link Type#castToGeneral(Type, GeneralCastingKind)}.
 */
public enum GeneralCastingKind implements ICastingKind {
	/** Оператор сравнения */
	COMPARASION,
	
	/** Операторы сравнения `==` и `!=` */
	EQUALS_COMPARASION,
	
	/** Любой бинарный оператор */
	BINARY_OPERATOR,
	
	/** Тернарный оператор */
	TERNARY_OPERATOR;
	
	private final String lowerCaseName;
	
	private GeneralCastingKind() {
		this.lowerCaseName = name().toLowerCase().replace('_', ' ');
	}
	
	@Override
	public String lowerCaseName() {
		return lowerCaseName;
	}
}
