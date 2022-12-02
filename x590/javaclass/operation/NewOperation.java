package x590.javaclass.operation;

import x590.javaclass.ClassInfo;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.ClassType;
import x590.javaclass.type.Type;

public class NewOperation extends Operation {
	
	protected final ClassType clazz;
	
	public NewOperation(DecompilationContext context, int index) {
		this(context.pool.getClassConstant(index).toClassType());
	}
	
	public NewOperation(ClassType clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(clazz);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print("new ").print(clazz, context.classinfo);
	}
	
	@Override
	public Type getReturnType() {
		return clazz;
	}
}