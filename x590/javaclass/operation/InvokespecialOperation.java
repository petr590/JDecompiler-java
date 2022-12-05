package x590.javaclass.operation;

import x590.javaclass.MethodDescriptor;
import x590.javaclass.Modifiers;
import x590.javaclass.context.DecompilationContext;
import x590.javaclass.context.StringifyContext;
import x590.javaclass.exception.DecompilationException;
import x590.javaclass.io.StringifyOutputStream;
import x590.javaclass.type.PrimitiveType;
import x590.javaclass.type.Type;

public class InvokespecialOperation extends InvokeNonstaticOperation {
	
	private final boolean isSuper;
	private final Type returnType;
	
	private boolean initIsSuper(DecompilationContext context) {
		return (context.modifiers & Modifiers.ACC_STATIC) == 0 &&
				descriptor.clazz.equals(context.classinfo.superType) &&
				object instanceof ALoadOperation aload && aload.index == 0;
	}
	
	private Type initReturnType(DecompilationContext context) {
		if(descriptor.isConstructor() && object.original() instanceof NewOperation newOperation) {
			if(context.stack.peek() != newOperation)
				throw new DecompilationException("Cannot invoke constructor of new object, invalid stack state");
			
			context.stack.pop();
			
			return newOperation.clazz;
		}
		
		return PrimitiveType.VOID;
	}
	
	public InvokespecialOperation(DecompilationContext context, int index) {
		super(context, index);
		this.isSuper = initIsSuper(context);
		this.returnType = initReturnType(context);
	}
	
	public InvokespecialOperation(DecompilationContext context, MethodDescriptor descriptor) {
		super(context, descriptor);
		this.isSuper = initIsSuper(context);
		this.returnType = initReturnType(context);
	}
	
	public InvokespecialOperation(DecompilationContext context, MethodDescriptor descriptor, Operation object) {
		super(context, descriptor, object);
		this.isSuper = initIsSuper(context);
		this.returnType = initReturnType(context);
	}
	
	@Override
	protected boolean canInvokeConstructor() {
		return true;
	}
	
	@Override
	protected String getInstructionName() {
		return "invokespecial";
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(descriptor.isConstructor()) {
			writeObject(out, context);
			writeArguments(out, context);
		} else {
			super.writeTo(out, context);
		}
	}
	
	@Override
	protected void writeObject(StringifyOutputStream out, StringifyContext context) {
		if(isSuper) {
			out.write("super");
		} else {
			out.write(object, context);
		}
	}
	
	@Override
	public Type getReturnType() {
		return returnType;
	}
	
	@Override
	public boolean canOmit() {
		return descriptor.isConstructor() && isSuper && arguments.isEmpty();
	}
}