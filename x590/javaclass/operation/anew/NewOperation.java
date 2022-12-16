package x590.javaclass.operation.anew;

import x590.javaclass.ClassInfo;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.Type;

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