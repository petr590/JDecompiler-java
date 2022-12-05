package x590.javaclass;

import x590.javaclass.attribute.Attributes;
import x590.javaclass.io.StringifyOutputStream;
import x590.jdecompiler.JDecompiler;

import static x590.javaclass.Modifiers.*;

public abstract class JavaClassElement implements StringWritableAndImportable {
	
	public boolean canStringify(ClassInfo classinfo) {
		return (getModifiers() & ACC_SYNTHETIC) == 0 || JDecompiler.getInstance().showSynthetic();
	}
	
	public abstract int getModifiers();
	
	public boolean isStatic() {
		return (getModifiers() & ACC_STATIC) != 0;
	}
	
	public boolean isFinal() {
		return (getModifiers() & ACC_FINAL) != 0;
	}
	
	public boolean isAbstract() {
		return (getModifiers() & ACC_ABSTRACT) != 0;
	}
	
	public boolean isSynchronized() {
		return (getModifiers() & ACC_SYNCHRONIZED) != 0;
	}
	
	public boolean isSynthetic() {
		return (getModifiers() & ACC_SYNTHETIC) != 0;
	}
	
	public boolean isBridge() {
		return (getModifiers() & ACC_BRIDGE) != 0;
	}
	
	
	protected static void writeAnnotations(StringifyOutputStream out, ClassInfo classinfo, Attributes attributes) {
		out.printIfNotNull(attributes.get("RuntimeVisibleAnnotations"), classinfo);
		out.printIfNotNull(attributes.get("RuntimeInvisibleAnnotations"), classinfo);
	}
}