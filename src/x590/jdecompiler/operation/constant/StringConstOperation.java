package x590.jdecompiler.operation.constant;

import x590.jdecompiler.JavaField;
import x590.jdecompiler.constpool.StringConstant;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.Operation;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.util.StringUtil;

public final class StringConstOperation extends ConstOperation {
	
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
					out.println().print(classinfo.getIndent()).print(StringUtil.toLiteral(lines[i] + "\n"));
					
					if(++i < length)
						out.write(" +");
					else
						break;
				}
				
				classinfo.reduceIndent(2);
				
				return;
			}
		}
		
		out.write(StringUtil.toLiteral(getValue()));
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	protected boolean canUseConstant(JavaField constant) {
		return super.canUseConstant(constant) && constant.getConstantValueAs(StringConstant.class).getString().equals(value);
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof StringConstOperation operation && value.equals(operation.value);
	}
}
