package x590.javaclass.operation;

import x590.javaclass.ClassInfo;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.BooleanOperation;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.ReferenceType;
import x590.javaclass.type.Types;

public class InstanceofOperation extends BooleanOperation {
	
	private final ReferenceType clazz;
	private final Operation object;
	
	public InstanceofOperation(DecompilationContext context, int index) {
		this.clazz = context.pool.getClassConstant(index).toReferenceType();
		this.object = context.stack.popAsNarrowest(Types.ANY_OBJECT_TYPE);
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(clazz);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print(object, context).print(" instanceof ").print(clazz, context.classinfo);
	}
}
