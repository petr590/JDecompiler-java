package x590.jdecompiler.modifiers;

import x590.jdecompiler.util.IWhitespaceStringBuilder;

public abstract class ClassEntryModifiers extends Modifiers {
	
	public ClassEntryModifiers(int value) {
		super(value);
	}
	
	
	public boolean isPublic() {
		return (value & ACC_PUBLIC) != 0;
	}
	
	public boolean isPrivate() {
		return (value & ACC_PRIVATE) != 0;
	}
	
	public boolean isProtected() {
		return (value & ACC_PROTECTED) != 0;
	}
	
	
	public boolean isStatic() {
		return (value & ACC_STATIC) != 0;
	}
	
	public boolean isFinal() {
		return (value & ACC_FINAL) != 0;
	}
	
	
	public boolean isNotPublic() {
		return (value & ACC_PUBLIC) == 0;
	}
	
	public boolean isNotPrivate() {
		return (value & ACC_PRIVATE) == 0;
	}
	
	public boolean isNotProtected() {
		return (value & ACC_PROTECTED) == 0;
	}
	
	
	public boolean isNotStatic() {
		return (value & ACC_STATIC) == 0;
	}
	
	public boolean isNotFinal() {
		return (value & ACC_FINAL) == 0;
	}
	
	
	@Override
	IWhitespaceStringBuilder toStringBuilder(boolean forWriting) {
		return super.toStringBuilder(forWriting)
				.appendIf(isPublic(), "public")
				.appendIf(isPrivate(), "private")
				.appendIf(isProtected(), "protected")
				.appendIf(isStatic(), "static")
				.appendIf(isFinal(), "final");
	}
}
