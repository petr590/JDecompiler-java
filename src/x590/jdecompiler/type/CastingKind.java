package x590.jdecompiler.type;

public enum CastingKind {
	/** Преобразование к наиболее узкому типу */
	NARROWEST(true),
	
	/** Преобразование к наиболее широкому типу */
	WIDEST(false);
	
	private final boolean isNarrowest, isWidest;
	
	private CastingKind(boolean isNarrowest) {
		this.isNarrowest = isNarrowest;
		this.isWidest = !isNarrowest;
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
