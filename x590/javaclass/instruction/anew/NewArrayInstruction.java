package x590.javaclass.instruction.anew;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.exception.DecompilationException;
import x590.javaclass.instruction.Instruction;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.anew.NewArrayOperation;
import x590.javaclass.type.ArrayType;
import x590.javaclass.util.Util;

public class NewArrayInstruction extends Instruction {
	
	private static ArrayType getArrayTypeByCode(int code) {
		switch(code) {
			case 0x4: return ArrayType.BOOLEAN_ARRAY;
			case 0x5: return ArrayType.CHAR_ARRAY;
			case 0x6: return ArrayType.FLOAT_ARRAY;
			case 0x7: return ArrayType.DOUBLE_ARRAY;
			case 0x8: return ArrayType.BYTE_ARRAY;
			case 0x9: return ArrayType.SHORT_ARRAY;
			case 0xA: return ArrayType.INT_ARRAY;
			case 0xB: return ArrayType.LONG_ARRAY;
			default:
				throw new DecompilationException("Illegal array type code 0x" + Util.hex(code));
		}
	}
	
	
	private final ArrayType type;
	
	public NewArrayInstruction(int code) {
		this.type = getArrayTypeByCode(code);
	}
	
	@Override
	public Operation toOperation(DecompilationContext context) {
		return new NewArrayOperation(context, type);
	}
}