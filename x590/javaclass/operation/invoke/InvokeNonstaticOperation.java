package x590.javaclass.operation.invoke;

import x590.javaclass.MethodDescriptor;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.operation.Operation;
import x590.javaclass.type.Types;

public abstract class InvokeNonstaticOperation extends InvokeOperation {
	
	protected final Operation object;
	
	public InvokeNonstaticOperation(DecompilationContext context, int index) {
		super(context, index, false);
		this.object = context.stack.popAsNarrowest(Types.ANY_TYPE);
	}
	
	public InvokeNonstaticOperation(DecompilationContext context, MethodDescriptor descriptor) {
		this(context, descriptor, context.stack.popAsNarrowest(Types.ANY_TYPE));
	}
	
	public InvokeNonstaticOperation(DecompilationContext context, MethodDescriptor descriptor, Operation object) {
		super(context, descriptor, false);
		this.object = object;
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(writeObject(out, context)) {
			out.write('.');
		}
		
		out.write(descriptor.name);
		writeArguments(out, context);
	}
	
	/** Возвращает {@literal true}, если объект записан */
	protected boolean writeObject(StringifyOutputStream out, StringifyContext context) {
		if(!canOmitObject(context, object)) {
			object.writeTo(out, context);
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean requiresLocalContext() {
		return object.requiresLocalContext() || super.requiresLocalContext();
	}
}