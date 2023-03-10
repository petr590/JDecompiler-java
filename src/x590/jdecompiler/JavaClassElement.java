package x590.jdecompiler;

import x590.jdecompiler.attribute.AttributeNames;
import x590.jdecompiler.attribute.Attributes;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.modifiers.Modifiers;

public abstract class JavaClassElement implements DisassemblingStringifyWritable<ClassInfo>, Importable {
	
	public boolean canStringify(ClassInfo classinfo) {
		return getModifiers().isNotSynthetic() || JDecompiler.getInstance().showSynthetic();
	}
	
	public abstract Modifiers getModifiers();
	
	
	protected static void writeAnnotations(StringifyOutputStream out, ClassInfo classinfo, Attributes attributes) {
		out .printIfNotNull(attributes.getNullable(AttributeNames.RUNTIME_VISIBLE_ANNOTATIONS), classinfo)
			.printIfNotNull(attributes.getNullable(AttributeNames.RUNTIME_INVISIBLE_ANNOTATIONS), classinfo);
	}
}
