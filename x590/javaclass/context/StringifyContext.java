package x590.javaclass.context;

import x590.javaclass.ClassInfo;
import x590.javaclass.MethodDescriptor;
import x590.javaclass.scope.MethodScope;

public class StringifyContext extends DecompilationAndStringifyContext {
	
	public StringifyContext(Context otherContext, ClassInfo classinfo, MethodDescriptor descriptor, MethodScope methodScope, int modifiers) {
		super(otherContext, classinfo, descriptor, methodScope, modifiers);
	}
	
	@Override
	public void warning(String message) {
		System.err.print("Stringify warning: ");
		System.err.println(message);
	}
}