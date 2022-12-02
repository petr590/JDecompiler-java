package x590.javaclass.operation;

import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.Type;

public class XorOperatorOperation extends BitwiseOperatorOperation {
	
	protected final boolean isBitNot;
	
	public XorOperatorOperation(Type type, DecompilationContext context) {
		super(type, context);
		this.isBitNot = operand2 instanceof IConstOperation iconst && iconst.value == -1;
	}
	
	@Override
	protected String getOperator() {
		return "^";
	}
	
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(isBitNot)
			out.print('~').printPrioritied(this, operand1, context, Associativity.RIGHT);
		else
			super.writeTo(out, context);
	}
	
	
	@Override
	public int getPriority() {
		return isBitNot ? Priority.UNARY : Priority.BIT_XOR;
	}
	
	@Override
	public int getVisiblePriority(Operation operand) {
		return isBitNot ? Priority.UNARY : super.getVisiblePriority(operand);
	}
}