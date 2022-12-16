package x590.javaclass.type;

public abstract class ReferenceType extends BasicType {
	
	public ReferenceType() {}
	
	public ReferenceType(String encodedName, String name) {
		super(encodedName, name);
	}
	
	@Override
	public final boolean isReferenceType() {
		return true;
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
		return this.canCastTo(other) ? this : null;
	}
	
	@Override
	protected Type castToWidestImpl(Type other) {
		return this.canCastTo(other) ? other : null;
	}
}