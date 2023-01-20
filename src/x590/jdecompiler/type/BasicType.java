package x590.jdecompiler.type;

public abstract class BasicType extends Type {
	
	String encodedName, name;
	
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
	
	@Override
	public final boolean isBasic() {
		return true;
	}
}