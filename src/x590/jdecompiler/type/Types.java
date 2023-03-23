package x590.jdecompiler.type;

/**
 * Содержит объявления некоторых типов-синглтонов для более удобного доступа
 */
public final class Types {
	
	private Types() {}
	
	public static final Type
			ANY_TYPE = AnyType.INSTANCE,
			ANY_OBJECT_TYPE = AnyObjectType.INSTANCE,
			EXCLUDING_BOOLEAN_TYPE = ExcludingBooleanType.INSTANCE;
}
