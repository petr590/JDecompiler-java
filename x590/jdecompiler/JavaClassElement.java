package x590.jdecompiler;

import x590.jdecompiler.attribute.Attributes;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.modifiers.Modifiers;

public abstract class JavaClassElement implements StringWritableAndImportable {
	
	public boolean canStringify(ClassInfo classinfo) {
		return getModifiers().isNotSynthetic() || JDecompiler.getInstance().showSynthetic();
	}
	
	public abstract Modifiers getModifiers();
	
	
	protected static void writeAnnotations(StringifyOutputStream out, ClassInfo classinfo, Attributes attributes) {
		out.printIfNotNull(attributes.getAsWritable("RuntimeVisibleAnnotations"), classinfo);
		out.printIfNotNull(attributes.getAsWritable("RuntimeInvisibleAnnotations"), classinfo);
	}
}