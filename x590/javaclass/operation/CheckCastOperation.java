package x590.javaclass.operation;

import x590.javaclass.ClassInfo;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.type.Types;

public class CheckCastOperation extends CastOperation {
	
	public CheckCastOperation(DecompilationContext context, int index) {
		super(Types.ANY_TYPE, context.pool.getClassConstant(index).toReferenceType(), false, context);
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		if(!implicitCast || !implicitCastAllowed)
			classinfo.addImport(castedType);
	}
}