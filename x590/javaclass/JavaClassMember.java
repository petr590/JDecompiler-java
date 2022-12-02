package x590.javaclass;

import x590.javaclass.io.ExtendedDataInputStream;

public abstract class JavaClassMember extends JavaClassElement {
	
	public final int modifiers;
	
	protected JavaClassMember(int modifiers) {
		this.modifiers = modifiers;
	}
	
	protected JavaClassMember(ExtendedDataInputStream in) {
		this(in.readUnsignedShort());
	}
	
	@Override
	public int getModifiers() {
		return modifiers;
	}
}