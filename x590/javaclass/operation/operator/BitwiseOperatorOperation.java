package x590.javaclass.operation.operator;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.operation.Priority;
import x590.javaclass.type.Type;
import x590.jdecompiler.JDecompiler;

public abstract class BitwiseOperatorOperation extends BinaryOperatorOperation {
	
	public BitwiseOperatorOperation(Type type, DecompilationContext context) {
		super(type, context);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(JDecompiler.getInstance().printBracketsAroundBitwiseOperands()) {
			out.printPrioritied(this, operand1, context, getVisiblePriority(operand1), Associativity.LEFT)
				.printsp().print(getOperator()).printsp()
				.printPrioritied(this, operand2, context, getVisiblePriority(operand2), Associativity.RIGHT);
		} else {
			super.writeTo(out, context);
		}
	}
	
	public int getVisiblePriority(Operation operand) {
		return operand.getPriority() > Priority.BIT_AND ?
				Priority.BITWISE_OPERAND : this.getPriority();
	}
}