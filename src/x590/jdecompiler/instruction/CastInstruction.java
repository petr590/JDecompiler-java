package x590.jdecompiler.instruction;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.operation.CastOperation;
import x590.jdecompiler.type.Type;

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
