package x590.jdecompiler.operation.load;

import java.util.List;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.ReturnableOperation;
import x590.jdecompiler.operation.store.AStoreOperation;
import x590.jdecompiler.type.ClassType;
import x590.jdecompiler.variable.Variable;
import x590.util.annotation.Immutable;
import x590.util.annotation.Nullable;

public class ExceptionLoadOperation extends ReturnableOperation {
	
	private final @Immutable List<ClassType> exceptionTypes;
	private @Nullable Variable exceptionVariable;
	
	public ExceptionLoadOperation(DecompilationContext context, @Immutable List<ClassType> exceptionTypes) {
		super(exceptionTypes.size() == 1 ? exceptionTypes.get(0) : ClassType.THROWABLE);
		
		this.exceptionTypes = exceptionTypes;
		
		context.onNextOperationDecompiling(operation -> {
			if(operation instanceof AStoreOperation astore)
				exceptionVariable = astore.getVariable();
			else
				context.warning("The first operation in the `catch` scope should be `astore`");
		});
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		classinfo.addImportsFor(exceptionTypes);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		out.printAll(exceptionTypes, context.getClassinfo(), " | ").printsp()
				.print(exceptionVariable != null ? exceptionVariable.getName() : "ex");
	}
	
	@Override
	public boolean equals(Operation other) {
		return this == other;
	}
}
