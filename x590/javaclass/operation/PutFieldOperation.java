package x590.javaclass.operation;

import x590.javaclass.constpool.FieldrefConstant;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;

public abstract class PutFieldOperation extends FieldOperation {
	
	protected final Operation value;
	
	private Operation getValue(DecompilationContext context) {
		Operation value = context.stack.popAsNarrowest(descriptor.type);
		value.addVariableName(descriptor.name);
		return value;
	}
	
	public PutFieldOperation(DecompilationContext context, int index) {
		super(context, index);
		this.value = getValue(context);
	}
	
	public PutFieldOperation(DecompilationContext context, FieldrefConstant fieldref) {
		super(context, fieldref);
		this.value = getValue(context);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.print(descriptor.name).print(" = ").print(value, context);
	}
	
	@Override
	public Type getReturnType() {
		return PrimitiveType.VOID;
	}
	
	@Override
	public boolean requiresLocalContext() {
		return value.requiresLocalContext();
	}
}