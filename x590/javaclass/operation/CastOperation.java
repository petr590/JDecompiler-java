package x590.javaclass.operation;

import x590.javaclass.ClassInfo;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.Type;

public class CastOperation extends Operation {
	
	private final Operation operand;
	protected final Type castedType;
	protected final boolean implicitCast;
	protected boolean implicitCastAllowed;
	
	public CastOperation(Type requiredType, Type castedType, boolean implicitCast, DecompilationContext context) {
		this.operand = context.stack.popAsNarrowest(requiredType);
		this.castedType = castedType;
		this.implicitCast = implicitCast;
	}
	
	public Operation getOperand() {
		return operand;
	}

	
	@Override
	public void addImports(ClassInfo classinfo) {
		if(!implicitCast || !implicitCastAllowed)
			classinfo.addImport(castedType);
	}
	
	
	@Override
	public void allowImplicitCast() {
		implicitCastAllowed = true;
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(implicitCast && implicitCastAllowed)
			out.write(operand, context);
		else
			out.print('(').print(castedType, context.classinfo).print(')')
					.printPrioritied(this, operand, context, Associativity.LEFT);
	}
	
	@Override
	public Type getReturnType() {
		return castedType;
	}
	
	public int getPriority() {
		return Priority.CAST;
	}
	
	@Override
	public boolean requiresLocalContext() {
		return operand.requiresLocalContext();
	}
}