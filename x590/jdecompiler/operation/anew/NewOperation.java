package x590.jdecompiler.operation.anew;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.Type;

public class NewOperation extends Operation {
	
	private final ClassType type;
	
	public NewOperation(DecompilationContext context, int index) {
		this(context.pool.getClassConstant(index).toClassType());
	}
	
	public NewOperation(ClassType type) {
		this.type = type;
	}
	
	public ClassType getType() {
		return type;
	}
	
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(type);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print("new ").print(type, context.classinfo);
	}
	
	@Override
	public Type getReturnType() {
		return type;
	}
}