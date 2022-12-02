package x590.javaclass.context;

import x590.javaclass.ClassInfo;
import x590.javaclass.MethodDescriptor;

public abstract class DecompilationAndStringifyContext extends Context {

	public final ClassInfo classinfo;
	public final MethodDescriptor descriptor;
	public final int modifiers;
	
	public DecompilationAndStringifyContext(Context otherContext, ClassInfo classinfo, MethodDescriptor descriptor, int modifiers) {
		super(otherContext);
		this.classinfo = classinfo;
		this.descriptor = descriptor;
		this.modifiers = modifiers;
	}
}