package x590.javaclass;

import x590.javaclass.io.ExtendedDataInputStream;

public abstract class JavaClassMember extends JavaClassElement {
	
	public final Modifiers modifiers;
	
	protected JavaClassMember(Modifiers modifiers) {
		this.modifiers = modifiers;
	}
	
	protected JavaClassMember(ExtendedDataInputStream in) {
		this(new Modifiers(in.readUnsignedShort()));
	}
	
	@Override
	public Modifiers getModifiers() {
		return modifiers;
	}
}