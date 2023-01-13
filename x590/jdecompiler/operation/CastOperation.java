package x590.jdecompiler.operation;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.WrapperClassType;

public class CastOperation extends Operation {
	
	private final Operation operand;
	private final Type requiredType, castedType;
	private final boolean implicitCast;
	private boolean implicitCastAllowed;
	
	public CastOperation(Type requiredType, Type castedType, boolean implicitCast, DecompilationContext context) {
		this(requiredType, castedType, implicitCast, context.popAsNarrowest(requiredType));
	}
	
	public CastOperation(Type requiredType, Type castedType, boolean implicitCast, Operation operand) {
		this.requiredType = requiredType;
		this.castedType = castedType;
		this.implicitCast = implicitCast;
		
		Type implicitType = operand.getImplicitType();
		
		if(implicitType.isWrapperClassType() && ((WrapperClassType)implicitType).getPrimitiveType().isImplicitSubtypeOf(castedType)) {
			operand.allowImplicitCast();
		} else if(operand instanceof CastOperation castOperation && castedType.isByteOrShortOrChar() && implicitType.isLongOrFloatOrDouble()) {
			operand = castOperation.getOperand();
		}
		
		this.operand = operand;
	}
	
	public Operation getOperand() {
		return operand;
	}
	
	public Type getRequiredType() {
		return requiredType;
	}
	
	public Type getCastedType() {
		return castedType;
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
					.printPrioritied(this, operand, context, Associativity.RIGHT);
	}
	
	@Override
	public Type getReturnType() {
		return castedType;
	}
	
	@Override
	public Type getImplicitType() {
		return operand.getReturnType();
	}
	
	@Override
	public int getPriority() {
		return implicitCast && implicitCastAllowed ? operand.getPriority() : Priority.CAST;
	}
	
	@Override
	public boolean requiresLocalContext() {
		return operand.requiresLocalContext();
	}
}
