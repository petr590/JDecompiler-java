package x590.javaclass.operation.invoke;

import x590.javaclass.ClassInfo;
import x590.javaclass.MethodDescriptor;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;

public class InvokestaticOperation extends InvokeOperation {
	
	public InvokestaticOperation(DecompilationContext context, int index) {
		super(context, index, true);
	}
	
	public InvokestaticOperation(DecompilationContext context, MethodDescriptor descriptor) {
		super(context, descriptor, true);
	}
	
	@Override
	protected String getInstructionName() {
		return "invokestatic";
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(!canOmitClass(context))
			out.print(descriptor.clazz, context.classinfo).print('.');
		
		out.write(descriptor.name);
		writeArguments(out, context);
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		super.addImports(classinfo);
		classinfo.addImport(descriptor.clazz);
	}
}