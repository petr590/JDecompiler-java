package x590.javaclass.instruction;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.operation.CastOperation;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.Type;

public class CastInstruction extends Instruction {
	
	protected final Type requiredType, castedType;
	protected final boolean implicitCast;
	
	public CastInstruction(Type requiredType, Type castedType, boolean implicitCast) {
		this.requiredType = requiredType;
		this.castedType = castedType;
		this.implicitCast = implicitCast;
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new CastOperation(requiredType, castedType, implicitCast, context);
	}
}