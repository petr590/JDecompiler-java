package x590.javaclass.operation;

import x590.javaclass.constpool.StringConstant;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.ClassType;
import x590.javaclass.util.Util;
import x590.jdecompiler.JDecompiler;

public class StringConstOperation extends ConstOperation {
	
	private final String value;
	
	public StringConstOperation(String value) {
		super(ClassType.STRING);
		this.value = value;
	}
	
	public StringConstOperation(StringConstant value) {
		this(value.getValue().getValue());
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
//		Operation operation = findConstant(constantContext);
//		if(operation != null)
//			return operation.toString(context);
		
		if(JDecompiler.getInstance().multilineStringAllowed()) {
			int lnPos = getValue().indexOf('\n');
			
			if(lnPos != -1 && lnPos != getValue().length() - 1) {
				var classinfo = context.classinfo;
				
				classinfo.increaseIndent(2);
				
				String[] lines = getValue().split("\n");
				
				for(int i = 0, length = lines.length;;) {
					out.println().print(classinfo.getIndent()).print(Util.toLiteral(lines[i] + "\n"));
					
					if(++i < length)
						out.write(" +");
					else
						break;
				}
				
				classinfo.reduceIndent(2);
				
				return;
			}
		}
		
		out.write(Util.toLiteral(getValue()));
	}

	public String getValue() {
		return value;
	}
}