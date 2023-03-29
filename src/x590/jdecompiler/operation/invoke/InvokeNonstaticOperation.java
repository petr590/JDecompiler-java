package x590.jdecompiler.operation.invoke;

import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.operation.Operation;

public abstract class InvokeNonstaticOperation extends InvokeOperation {
	
	protected final Operation object;
	
	public InvokeNonstaticOperation(DecompilationContext context, int index) {
		this(context, getDescriptor(context, index));
	}
	
	/** Не переделывать через делегирование конструктору this,
	 * так как важен порядок: сначала со стека снимаются аргументы, затем объект */
	public InvokeNonstaticOperation(DecompilationContext context, MethodDescriptor descriptor) {
		super(context, descriptor);
		this.object = context.popAsNarrowest(descriptor.getDeclaringClass()).castIfNull(descriptor.getDeclaringClass());
	}
	
	/** Только для случаев, когда у метода нет аргументов или когда объект не на стеке */
	public InvokeNonstaticOperation(DecompilationContext context, MethodDescriptor descriptor, Operation object) {
		super(context, descriptor);
		this.object = object.castIfNull(descriptor.getDeclaringClass());
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(writeObject(out, context)) {
			out.write('.');
		}
		
		out.print(descriptor.getName()).printUsingFunction(this, context, InvokeOperation::writeArguments);
	}
	
	/** Возвращает {@literal true}, если объект записан */
	protected boolean writeObject(StringifyOutputStream out, StringifyContext context) {
		if(!canOmitObject(context, object)) {
			out.printPrioritied(this, object, context, Associativity.LEFT);
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean requiresLocalContext() {
		return object.requiresLocalContext() || super.requiresLocalContext();
	}
}
