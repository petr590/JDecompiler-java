package x590.jdecompiler.operation.invoke;

import java.util.List;

import x590.jdecompiler.JavaMethod;
import x590.jdecompiler.MethodDescriptor;
import x590.jdecompiler.constpool.MethodTypeConstant;
import x590.jdecompiler.constpool.MethodrefConstant;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.ExtendedStringInputStream;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.type.Type;

public class LambdaOperation extends Operation {
	
	private final JavaMethod method;
	private final int captured;
	private final List<Operation> capturedArguments;
	
	public LambdaOperation(DecompilationContext context, List<Operation> capturedArguments, MethodrefConstant methodref, MethodTypeConstant methodType) {
		this.method = context.getClassinfo().getMethod(new MethodDescriptor(methodref));
		
		this.captured = Type.parseMethodArguments(new ExtendedStringInputStream(methodref.getNameAndType().getDescriptor().getString())).size() -
						Type.parseMethodArguments(new ExtendedStringInputStream(methodType.getDescriptor().getString())).size();
		
		this.capturedArguments = capturedArguments;
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		method.writeAsLambda(out, context.getClassinfo(), captured, capturedArguments);
	}
	
	@Override
	public Type getReturnType() {
		return ClassType.OBJECT;
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other;
	}
}
