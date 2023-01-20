package x590.jdecompiler.context;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.modifiers.MethodModifiers;
import x590.jdecompiler.scope.MethodScope;
import x590.util.Logger;

public class StringifyContext extends DecompilationAndStringifyContext {
	
	public StringifyContext(Context otherContext, ClassInfo classinfo, MethodDescriptor descriptor, MethodScope methodScope, MethodModifiers modifiers) {
		super(otherContext, classinfo, descriptor, methodScope, modifiers);
	}
	
	@Override
	public void warning(String message) {
		Logger.warning("Stringify warning " + message);
	}
}