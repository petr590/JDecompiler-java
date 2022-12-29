package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.ReferenceType;

public class InstanceofOperation extends BooleanOperation {
	
	private final ReferenceType clazz;
	
	public InstanceofOperation(DecompilationContext context, int index) {
		this.clazz = context.pool.getClassConstant(index).toReferenceType();
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print(clazz.toString(context.classinfo)).print(" instanceof ").print(clazz.toString(context.classinfo));
	}
}
