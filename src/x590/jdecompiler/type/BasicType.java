package x590.jdecompiler.type;

/**
 * Представляет Java тип (т.е. тип, который существует в коде)
 */
public abstract class BasicType extends Type {
	
	protected String encodedName, name;
	
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
