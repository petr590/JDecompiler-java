package x590.jdecompiler.operation.invoke;

import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.anew.NewOperation;
import x590.jdecompiler.operation.load.ALoadOperation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.Type;

public final class InvokespecialOperation extends InvokeNonstaticOperation {
	
	private final boolean isSuper, isEnum;
	private final Type returnType;
	
	private boolean initIsSuper(DecompilationContext context) {
		return context.modifiers.isNotStatic() &&
				descriptor.clazz.equals(context.classinfo.superType) &&
				object instanceof ALoadOperation aload && aload.getIndex() == 0;
	}
	
	private Type initReturnType(DecompilationContext context) {
		if(descriptor.isConstructor() && object instanceof NewOperation newOperation) {
			if(context.stackEmpty() || context.pop() != newOperation)
				throw new DecompilationException("Cannot invoke constructor of new object, invalid stack state");
			
			return newOperation.getType();
		}
		
		return PrimitiveType.VOID;
	}
	
	public InvokespecialOperation(DecompilationContext context, int index) {
		super(context, index);
		this.isSuper = initIsSuper(context);
		this.isEnum = context.classinfo.modifiers.isEnum();
		this.returnType = initReturnType(context);
	}
	
	public InvokespecialOperation(DecompilationContext context, MethodDescriptor descriptor) {
		super(context, descriptor);
		this.isSuper = initIsSuper(context);
		this.isEnum = context.classinfo.modifiers.isEnum();
		this.returnType = initReturnType(context);
	}
	
	public InvokespecialOperation(DecompilationContext context, MethodDescriptor descriptor, Operation object) {
		super(context, descriptor, object);
		this.isSuper = initIsSuper(context);
		this.isEnum = context.classinfo.modifiers.isEnum();
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
	protected boolean writeObject(StringifyOutputStream out, StringifyContext context) {
		if(isSuper) {
			out.write("super");
			return true;
		} else {
			return super.writeObject(out, context);
		}
	}
	
	@Override
	protected boolean canOmitObject(StringifyContext context, Operation object) {
		return !descriptor.isConstructor() && super.canOmitObject(context, object);
	}
	
	@Override
	public Type getReturnType() {
		return returnType;
	}
	
	@Override
	public boolean canOmit() {
		return descriptor.isConstructor() && isSuper &&
				(arguments.isEmpty() || isEnum && descriptor.argumentsEquals(ClassType.STRING, PrimitiveType.INT));
	}
}
