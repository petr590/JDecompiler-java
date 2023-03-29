package x590.jdecompiler.operation;

import x590.jdecompiler.Descriptor;
import x590.jdecompiler.context.StringifyContext;
import x590.jdecompiler.main.JDecompiler;
import x590.jdecompiler.operation.load.ALoadOperation;

public abstract class OperationWithDescriptor<D extends Descriptor> extends AbstractOperation {
	
	public final D descriptor;
	
	public OperationWithDescriptor(D descriptor) {
		this.descriptor = descriptor;
	}
	
	protected boolean canOmitClass(StringifyContext context) {
		return context.getClassinfo().canOmitClass(descriptor);
	}
	
	protected boolean canOmitObject(StringifyContext context, Operation object) {
		return JDecompiler.getConfig().canOmitThisAndClass() && context.getModifiers().isNotStatic() &&
				object instanceof ALoadOperation aload && aload.getIndex() == 0;
	}
	
	protected boolean equals(OperationWithDescriptor<D> other) {
		return descriptor.equals(other.descriptor);
	}
}
