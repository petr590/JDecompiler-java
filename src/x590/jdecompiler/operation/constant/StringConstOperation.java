package x590.jdecompiler.operation.constant;

import x590.jdecompiler.constpool.StringConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.util.StringUtil;

public final class StringConstOperation extends ConstOperation<StringConstant> {
	
	public StringConstOperation(StringConstant constant) {
		super(constant);
	}
	
	public StringConstOperation(DecompilationContext context, String value) {
		this(context.getClassinfo().getConstPool().findOrCreateConstant(value));
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		
		if(JDecompiler.getInstance().multilineStringAllowed()) {
			int lnPos = getValue().indexOf('\n');
			
			if(lnPos != -1 && lnPos != getValue().length() - 1) {
				out.increaseIndent(2);
				
				String[] lines = getValue().split("\n");
				
				for(int i = 0, length = lines.length;;) {
					out.println().printIndent().print(StringUtil.toLiteral(lines[i] + "\n"));
					
					if(++i < length)
						out.write(" +");
					else
						break;
				}
				
				out.reduceIndent(2);
				
				return;
			}
		}
		
		out.write(StringUtil.toLiteral(getValue()));
	}
	
	public String getValue() {
		return constant.getString();
	}
}
