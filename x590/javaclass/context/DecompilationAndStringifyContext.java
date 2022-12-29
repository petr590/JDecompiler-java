package x590.javaclass.context;

import x590.javaclass.ClassInfo;
import x590.javaclass.MethodDescriptor;
import x590.javaclass.Modifiers;
import x590.javaclass.scope.MethodScope;

public abstract class DecompilationAndStringifyContext extends Context {
	
	public final ClassInfo classinfo;
	public final MethodDescriptor descriptor;
	public final MethodScope methodScope;
	public final Modifiers modifiers;
	
	public DecompilationAndStringifyContext(Context otherContext, ClassInfo classinfo, MethodDescriptor descriptor, MethodScope methodScope, Modifiers modifiers) {
		super(otherContext);
		this.classinfo = classinfo;
		this.descriptor = descriptor;
		this.methodScope = methodScope;
		this.modifiers = modifiers;
	}
}
