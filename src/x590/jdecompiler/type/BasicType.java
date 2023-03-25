package x590.jdecompiler.type;

/**
 * Представляет Java тип (т.е. определённый тип, который существует в коде)
 */
public abstract class BasicType extends Type {
	
	/** "Ljava/lang/Object;", "I" */
	protected String encodedName;
	
	/** "java.lang.Object", "int" */
	protected String name;
	
	/** Если подклассы вызывают этот конструктор, то они должны сами
	 * инициализировать поля {@link #encodedName} и {@link #name} */
	public BasicType() {}
	
	public BasicType(String encodedName, String name) {
		this.encodedName = encodedName;
		this.name = name;
	}
	
	@Override
	public final String getEncodedName() {
		return encodedName;
	}
	
	@Override
	public final String getName() {
		return name;
	}
}
