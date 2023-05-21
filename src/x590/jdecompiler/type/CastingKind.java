package x590.jdecompiler.type;

/**
 * Вид преобразования
 */
public enum CastingKind implements ICastingKind {
	/** Преобразование к наиболее узкому типу */
	NARROWEST(true),
	
	/** Преобразование к наиболее широкому типу */
	WIDEST(false);
	
	private final boolean isNarrowest, isWidest;
	private final String lowerCaseName;
	
	private CastingKind(boolean isNarrowest) {
		this.isNarrowest = isNarrowest;
		this.isWidest = !isNarrowest;
		this.lowerCaseName = name().toLowerCase();
	}
	
	@Override
	public String lowerCaseName() {
		return lowerCaseName;
	}
	
	public boolean toBoolean() {
		return isWidest;
	}
	
	public boolean isNarrowest() {
		return isNarrowest;
	}
	
	public boolean isWidest() {
		return isWidest;
	}
}
