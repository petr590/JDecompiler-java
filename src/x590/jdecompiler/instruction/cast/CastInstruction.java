package x590.jdecompiler.instruction.cast;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.instruction.Instruction;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.cast.CastOperation;
import x590.jdecompiler.type.Type;

public class CastInstruction implements Instruction {
	
	protected final Type requiredType, castedType;
	protected final boolean implicitCast;
	
	public CastInstruction(Type requiredType, Type castedType, boolean implicitCast) {
		this.requiredType = requiredType;
		this.castedType = castedType;
		this.implicitCast = implicitCast;
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return CastOperation.of(requiredType, castedType, implicitCast, context);
	}
}
