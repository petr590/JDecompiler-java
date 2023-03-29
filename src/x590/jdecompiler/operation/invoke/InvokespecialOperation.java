package x590.jdecompiler.operation.invoke;

import java.util.LinkedList;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.clazz.JavaClass;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.operation.NewOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.load.ALoadOperation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.PrimitiveType;
import x590.jdecompiler.type.ReferenceType;
import x590.jdecompiler.type.Type;
import x590.util.annotation.Nullable;

public final class InvokespecialOperation extends InvokeNonstaticOperation {
	
	/** Какой метод мы вызываем через super */
	private static enum SuperState {
		SUPERCLASS,     // метод суперкласса
		SUPERINTERFACE, // метод суперинтерфейса
		NONE;           // не через super
	}
	
	private final SuperState superState;
	private final boolean isEnum;
	private final Type returnType;
	private final @Nullable JavaClass anonymousClass;
	
	private SuperState getSuperState(DecompilationContext context) {
		if(context.getModifiers().isNotStatic() &&
			object instanceof ALoadOperation aload && aload.getIndex() == 0) {
			
			ReferenceType clazz = descriptor.getDeclaringClass();
			
			if(clazz.equals(context.getClassinfo().getSuperType()))
				return SuperState.SUPERCLASS;
			
			if(context.getClassinfo().getInterfaces().stream().anyMatch(interfaceType -> clazz.equals(interfaceType)))
				return SuperState.SUPERINTERFACE;
		}
		
		return SuperState.NONE;
	}
	
	private Type getReturnType(DecompilationContext context) {
		if(descriptor.isConstructor() && object instanceof NewOperation newOperation) {
			
			if(context.pop() != newOperation)
				throw new DecompilationException("Cannot invoke constructor of object that is not new, invalid stack state");
			
			return newOperation.getType();
		}
		
		return descriptor.getReturnType();
	}
	
	private JavaClass getAnonymousClass() {
		if(descriptor.isConstructor() && returnType instanceof ClassType classType && classType.isAnonymous()) {
			JavaClass anonymousClass = JavaClass.find(classType);
			
			if(anonymousClass != null) {
				anonymousClass.decompile();
			}
			
			return anonymousClass;
		}
		
		return null;
	}
	
	public InvokespecialOperation(DecompilationContext context, int index) {
		super(context, index);
		this.superState = getSuperState(context);
		this.isEnum = context.getClassinfo().getModifiers().isEnum();
		this.returnType = getReturnType(context);
		this.anonymousClass = getAnonymousClass();
	}
	
	public InvokespecialOperation(DecompilationContext context, MethodDescriptor descriptor) {
		super(context, descriptor);
		this.superState = getSuperState(context);
		this.isEnum = context.getClassinfo().getModifiers().isEnum();
		this.returnType = getReturnType(context);
		this.anonymousClass = getAnonymousClass();
	}
	
	public InvokespecialOperation(DecompilationContext context, MethodDescriptor descriptor, Operation object) {
		super(context, descriptor, object);
		this.superState = getSuperState(context);
		this.isEnum = context.getClassinfo().getModifiers().isEnum();
		this.returnType = getReturnType(context);
		this.anonymousClass = getAnonymousClass();
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
	public @Nullable LinkedList<Operation> getStringBuilderChain(LinkedList<Operation> operands) {
		if(descriptor.isConstructor() &&
				descriptor.getDeclaringClass().equals(ClassType.STRING_BUILDER)) {
			
			if(descriptor.argumentsEmpty()) {
				return operands;
			}
			
			if(descriptor.argumentsEquals(ClassType.STRING)) {
				Operation firstArg = getArguments().getFirst();
				
				if(firstArg instanceof InvokestaticOperation invokestatic &&
						invokestatic.descriptor.equals(ClassType.STRING, "valueOf", ClassType.STRING, 1)) {
					
					firstArg = invokestatic.getArguments().getFirst();
				}
				
				operands.addFirst(firstArg);
				return operands;
			}
		}
		
		return null;
	}
	
	
	@Override
	public void addImports(ClassInfo classino) {
		if(anonymousClass != null) {
			classino.addImport(returnType);
			anonymousClass.addImports(classino);
		}
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(descriptor.isConstructor()) {
			if(anonymousClass != null) {
				anonymousClass.writeAsNewAnonymousObject(out, context, this);
				
			} else {
				writeObject(out, context);
				writeArguments(out, context);
			}
			
		} else {
			super.writeTo(out, context);
		}
	}
	
	@Override
	protected int skipArguments() {
		return descriptor.isConstructor() && isEnum ? MethodDescriptor.IMPLICIT_ENUM_ARGUMENTS :
				anonymousClass != null ? 1 : 0;
	}
	
	@Override
	public void writeArguments(StringifyOutputStream out, StringifyContext context) {
		super.writeArguments(out, context);
	}
	
	@Override
	protected boolean writeObject(StringifyOutputStream out, StringifyContext context) {
		
		return switch(superState) {
			
			case SUPERCLASS -> {
				out.write("super");
				yield true;
			}
			
			case SUPERINTERFACE -> {
				out.print(descriptor.getDeclaringClass(), context.getClassinfo()).print(".super");
				yield true;
			}
			
			case NONE -> super.writeObject(out, context);
		};
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
		return descriptor.isConstructor() && superState == SuperState.SUPERCLASS &&
				(isArgumentsEmpty() || isEnum && descriptor.argumentsEquals(ClassType.STRING, PrimitiveType.INT));
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other || other instanceof InvokespecialOperation operation &&
				super.equals(operation) && returnType.equals(operation.returnType);
	}
}
