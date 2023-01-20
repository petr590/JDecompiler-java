package x590.jdecompiler.type;

public class Types {
	
	private Types() {}
	
	public static final SpecialType
			ANY_TYPE = AnyType.INSTANCE,
			ANY_OBJECT_TYPE = AnyObjectType.INSTANCE,
			EXCLUDING_BOOLEAN_TYPE = ExcludingBooleanType.INSTANCE;
}
