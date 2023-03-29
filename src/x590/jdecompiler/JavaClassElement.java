package x590.jdecompiler;

import static x590.jdecompiler.modifiers.Modifiers.ACC_ACCESS_FLAGS;
import static x590.jdecompiler.modifiers.Modifiers.ACC_PRIVATE;
import static x590.jdecompiler.modifiers.Modifiers.ACC_PROTECTED;
import static x590.jdecompiler.modifiers.Modifiers.ACC_PUBLIC;
import static x590.jdecompiler.modifiers.Modifiers.ACC_VISIBLE;

import x590.jdecompiler.attribute.AttributeType;
import x590.jdecompiler.attribute.Attributes;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.exception.IllegalModifiersException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.modifiers.ClassEntryModifiers;
import x590.jdecompiler.util.IWhitespaceStringBuilder;

public abstract class JavaClassElement implements DisassemblingStringifyWritable<ClassInfo>, Importable, JavaSerializable {
	
	protected static final String ILLEGAL_ACCESS_MODIFIERS_MESSAGE = "illegal access modifiers";
	
	public boolean canStringify(ClassInfo classinfo) {
		return getModifiers().isNotSynthetic() || JDecompiler.getConfig().showSynthetic();
	}
	
	public abstract ClassEntryModifiers getModifiers();
	
	
	protected static void writeAnnotations(StringifyOutputStream out, ClassInfo classinfo, Attributes attributes) {
		out .printIfNotNull(attributes.getNullable(AttributeType.RUNTIME_VISIBLE_ANNOTATIONS), classinfo)
			.printIfNotNull(attributes.getNullable(AttributeType.RUNTIME_INVISIBLE_ANNOTATIONS), classinfo);
	}
	
	public abstract String getModifiersTarget();
	
	protected void baseModifiersToString(IWhitespaceStringBuilder str) {
		var modifiers = getModifiers();
		
		switch(modifiers.and(ACC_ACCESS_FLAGS)) {
			case ACC_VISIBLE   -> {}
			case ACC_PRIVATE   -> str.append("private");
			case ACC_PROTECTED -> str.append("protected");
			case ACC_PUBLIC    -> str.append("public");
			
			default ->
				throw new IllegalModifiersException(this, modifiers, ILLEGAL_ACCESS_MODIFIERS_MESSAGE);
		}
		
		if(modifiers.isStatic()) {
			str.append("static");
		}
	}
}
