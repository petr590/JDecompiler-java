package x590.javaclass.operation;

import x590.javaclass.Variable;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;

public class IIncOperatin extends Operation {
	
	private final Variable variable;
	private final int value;
	
	public IIncOperatin(DecompilationContext context, int index, int value) {
		this.variable = context.currentScope().getDefinedVariable(index);
		this.value = value;
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.write(variable.getName());
		
		if(value < 0)
			out.write(value == -1 ? "--" : " -= " + value);
		else
			out.write(value == 1 ? "++" : " += " + value);
	}
	
	@Override
	public Type getReturnType() {
		return PrimitiveType.VOID;
	}
	
	@Override
	public int getPriority() {
		return Priority.POST_INCREMENT;
	}
}