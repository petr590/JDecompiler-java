package x590.jdecompiler.operation.invoke;

import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.Types;

public abstract class InvokeNonstaticOperation extends InvokeOperation {
	
	protected final Operation object;
	
	public InvokeNonstaticOperation(DecompilationContext context, int index) {
		this(context, getDescriptor(context, index));
	}
	
	// Не переделывать через делегирование конструктору this,
	// так как важен порядок: сначала со стека снимаются аргументы, затем объект
	public InvokeNonstaticOperation(DecompilationContext context, MethodDescriptor descriptor) {
		super(context, descriptor, false);
		this.object = context.stack.popAsNarrowest(Types.ANY_TYPE);
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
