package x590.jdecompiler.context;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.modifiers.MethodModifiers;
import x590.jdecompiler.scope.MethodScope;

public abstract class DecompilationAndStringifyContext extends Context {
	
	public final ClassInfo classinfo;
	public final MethodDescriptor descriptor;
	public final MethodModifiers modifiers;
	public final MethodScope methodScope;
	
	public DecompilationAndStringifyContext(Context otherContext, ClassInfo classinfo, MethodDescriptor descriptor, MethodModifiers modifiers, MethodScope methodScope) {
		super(otherContext);
		this.classinfo = classinfo;
		this.descriptor = descriptor;
		this.methodScope = methodScope;
		this.modifiers = modifiers;
	}
}
