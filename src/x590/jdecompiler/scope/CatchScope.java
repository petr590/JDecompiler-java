package x590.jdecompiler.scope;

import java.util.List;

import x590.jdecompiler.clazz.ClassInfo;
import x590.jdecompiler.context.DecompilationContext;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.load.ExceptionLoadOperation;
import x590.jdecompiler.type.ClassType;
import x590.util.annotation.Immutable;

public class CatchScope extends Scope {
	
	private final ExceptionLoadOperation loadOperation;
	private final boolean hasNext;
	
	public CatchScope(DecompilationContext context, int endIndex, @Immutable List<ClassType> exceptionTypes, boolean hasNext) {
		super(context, context.currentIndex() - 1, Math.min(endIndex, context.currentScope().endIndex()));
		
		this.loadOperation = new ExceptionLoadOperation(context, exceptionTypes);
		context.push(loadOperation);
		
		this.hasNext = hasNext;
	}
	
	@Override
	public void addOperation(Operation operation, int fromIndex) {
		super.addOperation(operation, fromIndex);
		if(operation.isTerminable()) {
			setEndIndex(fromIndex);
		}
	}
	
	// Maybe TODO
	@Override
	public boolean isTerminable() {
		return false;
	}
	
	@Override
	protected boolean canOmitCurlyBrackets() {
		return false;
	}
	
	@Override
	protected void writeHeader(StringifyOutputStream out, StringifyContext context) {
		out.print("catch(").print(loadOperation, context).print(')');
	}
	
	@Override
	public void writeFront(StringifyOutputStream out, StringifyContext context) {
		out.printsp();
	}
	
	@Override
	public void writeSeparator(StringifyOutputStream out, StringifyContext context, Operation nextOperation) {
		if(!hasNext)
			out.println();
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		loadOperation.addImports(classinfo);
	}
}
