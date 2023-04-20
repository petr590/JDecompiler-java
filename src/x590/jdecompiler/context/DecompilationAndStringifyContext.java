package x590.jdecompiler.context;

import x590.jdecompiler.attribute.CodeAttribute;
import x590.jdecompiler.attribute.CodeAttribute.ExceptionTable;
import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.method.JavaMethod;
import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.modifiers.MethodModifiers;
import x590.jdecompiler.scope.MethodScope;

public abstract class DecompilationAndStringifyContext extends Context {
	
	private final ClassInfo classinfo;
	private final JavaMethod method;
	
	public DecompilationAndStringifyContext(Context otherContext, ClassInfo classinfo, JavaMethod method) {
		super(otherContext);
		this.classinfo = classinfo;
		this.method = method;
	}
	
	public ClassInfo getClassinfo() {
		return classinfo;
	}
	
	public JavaMethod getMethod() {
		return method;
	}
	
	public MethodDescriptor getDescriptor() {
		return method.getDescriptor();
	}
	
	public MethodModifiers getMethodModifiers() {
		return method.getModifiers();
	}
	
	public MethodScope getMethodScope() {
		return method.getMethodScope();
	}
	
	public CodeAttribute getCodeAttribute() {
		return method.getCodeAttribute();
	}
	
	public ExceptionTable getExceptionTable() {
		return method.getCodeAttribute().getExceptionTable();
	}
}
