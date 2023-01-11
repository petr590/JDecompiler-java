package x590.jdecompiler.operation;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.ReferenceType;
import x590.jdecompiler.type.Types;

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
