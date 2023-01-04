package x590.javaclass;

import x590.javaclass.attribute.Attributes;
import x590.javaclass.io.StringifyOutputStream;
import x590.jdecompiler.JDecompiler;

public abstract class JavaClassElement implements StringWritableAndImportable {
	
	public boolean canStringify(ClassInfo classinfo) {
		return !getModifiers().isSynthetic() || JDecompiler.getInstance().showSynthetic();
	}
	
	public abstract Modifiers getModifiers();
	
	
	protected static void writeAnnotations(StringifyOutputStream out, ClassInfo classinfo, Attributes attributes) {
		out.printIfNotNull(attributes.getAsWritable("RuntimeVisibleAnnotations"), classinfo);
		out.printIfNotNull(attributes.getAsWritable("RuntimeInvisibleAnnotations"), classinfo);
	}
}
