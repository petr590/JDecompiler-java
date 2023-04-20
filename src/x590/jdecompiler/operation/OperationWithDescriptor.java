package x590.jdecompiler.operation;

import x590.jdecompiler.Descriptor;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.main.JDecompiler;

public abstract class OperationWithDescriptor<D extends Descriptor> extends AbstractOperation {
	
	protected final D descriptor;
	
	public OperationWithDescriptor(D descriptor) {
		this.descriptor = descriptor;
	}
	
	public D getDescriptor() {
		return descriptor;
	}
	
	protected boolean canOmitClass(StringifyContext context) {
		return context.getClassinfo().canOmitClass(descriptor);
	}
	
	protected boolean canOmitObject(StringifyContext context, Operation object) {
		return JDecompiler.getConfig().canOmitThisAndClass() && object.isThisObject(context.getMethodModifiers());
	}
	
	protected boolean equals(OperationWithDescriptor<D> other) {
		return descriptor.equals(other.descriptor);
	}
}
