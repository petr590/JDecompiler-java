package x590.javaclass.operation.constant;

import x590.javaclass.JavaField;
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
		this(value.getString());
	}
	
	@Override
	public void writeValue(StringifyOutputStream out, StringifyContext context) {
		
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
	
	@Override
	protected boolean canUseConstant(JavaField constant) {
		return super.canUseConstant(constant) && ((StringConstant)constant.constantValueAttribute.value).getString().equals(value);
	}
}