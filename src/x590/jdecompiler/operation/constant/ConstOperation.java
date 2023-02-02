package x590.jdecompiler.operation.constant;

import x590.jdecompiler.ClassInfo;
import x590.jdecompiler.constpool.ConstValueConstant;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.io.StringifyOutputStream;
import x590.jdecompiler.operation.Operation;
import x590.jdecompiler.operation.ReturnableOperation;

public abstract class ConstOperation<C extends ConstValueConstant> extends ReturnableOperation {
	
	protected final C constant;
	
	public ConstOperation(C constant) {
		super(constant.getType());
		this.constant = constant;
	}
	
	public C getConstant() {
		return constant;
	}
	
	@Override
	public final boolean isOne() {
		return constant.isOne();
	}
	
	@Override
	public void addImports(ClassInfo classinfo) {
		constant.addImports(classinfo);
	}
	
	@Override
	public void writeTo(StringifyOutputStream out, StringifyContext context) {
		constant.writeTo(out, context.classinfo, returnType);
	}
	
	@Override
	public int getPriority() {
		return constant.getPriority();
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public boolean equals(Operation other) {
		return this == other || other instanceof ConstOperation operation && constant.equals(operation.constant);
	}
}
