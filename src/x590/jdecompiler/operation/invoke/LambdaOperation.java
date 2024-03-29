package x590.jdecompiler.operation.invoke;

import java.util.List;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.constpool.MethodHandleConstant;
import x590.jdecompiler.constpool.MethodHandleConstant.ReferenceKind;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.exception.DecompilationException;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.method.JavaMethod;
import x590.jdecompiler.method.MethodDescriptor;
import x590.jdecompiler.operation.AbstractOperation;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.OperationUtils;
import x590.jdecompiler.operation.Priority;
import x590.jdecompiler.operation.cast.CastOperation;
import x590.jdecompiler.type.Type;
import x590.jdecompiler.type.Types;
import x590.jdecompiler.type.reference.ReferenceType;
import x590.util.annotation.Nullable;

public class LambdaOperation extends AbstractOperation {
	
	private final MethodDescriptor descriptor;
	private final @Nullable JavaMethod method;
	private final ReferenceKind referenceKind;
	private final int captured;
	private final List<Operation> capturedArguments;
	
	public LambdaOperation(DecompilationContext context, MethodDescriptor lambdaDescriptor, List<Operation> capturedArguments, MethodHandleConstant methodHandle) {
		this.descriptor = methodHandle.getMethodrefConstant().toDescriptor();
		
		var classinfo = context.getClassinfo();
		
		this.method = descriptor.getDeclaringClass().equals(classinfo.getThisType()) ?
				getMethod(classinfo, descriptor) : null;
		
		this.referenceKind = methodHandle.getReferenceKind();
		
		if(method == null && capturedArguments.size() != referenceKind.argumentsForLambdaReference()) {
			throw new DecompilationException("Lambda with method reference with " + methodHandle.getReferenceKind()
					+ " reference kind cannot have " + capturedArguments.size() + " arguments");
		}
		
		this.captured = lambdaDescriptor.getArgumentsCount();
		
		this.capturedArguments = capturedArguments;
		
		assert capturedArguments.size() == captured :
			capturedArguments + ".size() != " + captured;
		
		if(!capturedArguments.isEmpty())
			OperationUtils.removeIfAutogeneratedCheckForNull(context, capturedArguments.get(0));
	}
	
	private @Nullable JavaMethod getMethod(ClassInfo classinfo, MethodDescriptor descriptor) {
		return classinfo.findMethod(descriptor).filter(method -> method.getModifiers().isSynthetic()).orElse(null);
	}
	
	
	@Override
	public Operation castIfNecessary(ReferenceType clazz) {
		return CastOperation.of(Types.ANY_OBJECT_TYPE, clazz, false, this);
	}
	
	
	@Override
	public int getPriority() {
		return method != null ? method.getLambdaPriority() : Priority.DEFAULT_PRIORITY;
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		if(method != null) {
			method.addImports(classinfo);
		} else {
			if(capturedArguments.isEmpty())
				classinfo.addImport(descriptor.getDeclaringClass());
		}
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		if(method != null) {
			method.writeAsLambda(out, captured, capturedArguments);
			
		} else {
			if(!capturedArguments.isEmpty()) {
				out.printPrioritied(this, capturedArguments.get(0).castIfNecessary(descriptor.getDeclaringClass()), context, Associativity.LEFT);
			} else {
				out.print(descriptor.getDeclaringClass(), context.getClassinfo());
			}
			
			out.print("::").print(referenceKind == ReferenceKind.NEWINVOKESPECIAL ? "new" : descriptor.getName());
		}
	}
	
	@Override
	public Type getReturnType() {
		return Types.ANY_OBJECT_TYPE;
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other;
	}
}
