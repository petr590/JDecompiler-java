package x590.javaclass.operation;

import x590.javaclass.ClassInfo;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.variable.Variable;

public class VariableDefineOperation extends VoidOperation {
	
	private final Variable variable;
	
	public VariableDefineOperation(Variable variable) {
		this.variable = variable;
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImport(variable.getType());
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.printsp(variable.getType(), context.classinfo).print(variable.getName());
	}
}