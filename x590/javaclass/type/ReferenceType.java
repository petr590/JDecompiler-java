package x590.javaclass.type;

public abstract class ReferenceType extends BasicType {
	
	public ReferenceType() {}
	
	public ReferenceType(String encodedName, String name) {
		super(encodedName, name);
	}
	
	@Override
	public TypeSize getSize() {
		return TypeSize.FOUR_BYTES;
	}
	
	public String getClassEncodedName() {
		return encodedName;
	}
	
	
	@Override
	protected Type castToNarrowestImpl(Type other) {
		return this.isSubtypeOf(other) ? this : null;
	}
	
	@Override
	protected Type castToWidestImpl(Type other) {
		return this.isSubtypeOf(other) ? other : null;
	}
}